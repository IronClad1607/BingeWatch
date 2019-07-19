package com.example.bingewatch.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bingewatch.R
import com.example.bingewatch.auth.models.AuthRequestToken
import com.example.bingewatch.auth.models.SessionResponseBody
import com.example.bingewatch.networks.getGuestSession
import com.example.bingewatch.networks.getTokenRequest
import com.example.bingewatch.networks.postAuthTokenRequest
import com.example.bingewatch.networks.postSession
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {


    val superviser = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + superviser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnGuest.setOnClickListener {
            launch {
                val session = getGuestSession()
                Log.i("Guest", "$session")

            }
        }

        btnLogIn.setOnClickListener {
            launch {
                val token = getTokenRequest()

                val authRequestBody =
                    AuthRequestToken(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        token?.request_token
                    )

                val authResponse = postAuthTokenRequest(authRequestBody)


                val requestToken = authResponse?.request_token //Authorized RequestToken
                val sessionResponseBody = SessionResponseBody(requestToken)

                val sessionResponse = postSession(sessionResponseBody)

                Log.d("Session","$sessionResponse")
                if (sessionResponse != null) {
                    val logInIntent = Intent(this@MainActivity, MoviesActivity::class.java)
                    logInIntent.putExtra("session_id", sessionResponse.session_id)
                    startActivity(logInIntent)
                } else {
                    Toast.makeText(this@MainActivity, "Invalid Username or Password!", Toast.LENGTH_LONG).show()
                }
            }
        }


        btnSignUp.setOnClickListener {
            val signUpIntent = Intent()
            signUpIntent.action = Intent.ACTION_VIEW
            signUpIntent.data = Uri.parse("https://www.themoviedb.org/account/signup")
            startActivity(signUpIntent)

            Log.d("Signup", "$signUpIntent")
        }
    }
}