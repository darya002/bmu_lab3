package com.example.bmu_3lab

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import android.widget.ImageView
import com.bumptech.glide.Glide
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendSecureRequest()
    }

    private fun sendSecureRequest() {
        val request = Request.Builder()
            .url("https://reqres.in/api/users/2")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("TLS", "Ошибка соединения: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.e("TLS", "Ошибка: ${it.code}")
                    } else {
                        val json = JSONObject(it.body?.string() ?: "")
                        val data = json.getJSONObject("data")

                        val firstName = data.getString("first_name")
                        val lastName = data.getString("last_name")
                        val email = data.getString("email")
                        val avatar = data.getString("avatar")

                        runOnUiThread {
                            findViewById<TextView>(R.id.nameTextView).text = "$firstName $lastName"
                            findViewById<TextView>(R.id.emailTextView).text = email
                            Glide.with(this@MainActivity)
                                .load(avatar)
                                .circleCrop()
                                .into(findViewById<ImageView>(R.id.avatarImageView))
                        }
                    }
                }
            }
        })
    }
}



