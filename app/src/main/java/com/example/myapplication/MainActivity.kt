package com.example.myapplication

import android.content.Intent
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var currentUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        memeLoad()
    }

    private fun memeLoad(){

        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE
        nextBtn.isEnabled = false
        shareBtn.isEnabled = false
        val  url = "https://meme-api.herokuapp.com/gimme"
        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentUrl = response.getString("url")
                Glide.with(this).load(currentUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        nextBtn.isEnabled = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        shareBtn.isEnabled = true
                        nextBtn.isEnabled = true
                        return false
                    }

                }).into(memeView)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            })
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }

    fun onClickedShare(view: View){

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Checkout this meme $currentUrl")
        val chooser = Intent.createChooser(intent, "Share this meme...")
        startActivity(chooser)
    }
    fun onClickedNext(view: View){
        memeLoad()
    }
}