package com.example.bingewatch.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.bingewatch.R
import com.example.bingewatch.auth.AuthRequestToken
import com.example.bingewatch.auth.SessionResponse
import com.example.bingewatch.auth.SessionResponseBody
import com.example.bingewatch.networks.getGuestSession
import com.example.bingewatch.networks.getTokenRequest
import com.example.bingewatch.networks.postAuthTokenRequest
import com.example.bingewatch.networks.postSession
import com.unstoppable.submitbuttonview.SubmitButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {


    var sessionResponse: SessionResponse? = null
    private val supervisor = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = getPreferences(Context.MODE_PRIVATE)


        val retainLoginInIntent = Intent(this, MajorActivity::class.java)
        if (pref.contains("username") && pref.contains("password")) {
            startActivity(retainLoginInIntent)
        }


        btnGuest.setOnClickListener {
            launch {
                val session = getGuestSession()
                Log.i("Guest", "$session")

            }
        }

        btnLogin.setOnClickListener {
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

                sessionResponse = postSession(sessionResponseBody)

                Log.d("Session", "$sessionResponse")
                if (sessionResponse != null) {
                    pref.edit {
                        putString("username", etEmail.text.toString())
                        putString("password", etPassword.text.toString())
                        putString("sessionId", sessionResponse!!.session_id)
                    }
                    btnLogin.doResult(true)
                    val logInIntent = Intent(this@LoginActivity, MajorActivity::class.java)
                    logInIntent.putExtra("session_id", sessionResponse!!.session_id)
                    startActivity(logInIntent)
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid Username or Password!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnSignUp.setOnClickListener {
            val signUpIntent = Intent()
            signUpIntent.action = Intent.ACTION_VIEW
            signUpIntent.data = Uri.parse("https://www.themoviedb.org/account/signup")
            startActivity(signUpIntent)
            btnSignUp.reset()

            Log.d("Signup", "$signUpIntent")
        }
    }
}
