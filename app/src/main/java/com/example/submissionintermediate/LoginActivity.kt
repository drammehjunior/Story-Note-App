package com.example.submissionintermediate

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submissionintermediate.Api.ApiConfig
import com.example.submissionintermediate.databinding.ActivityLoginBinding
import com.example.submissionintermediate.pojo.ResponseLogin
import com.example.submissionintermediate.utils.verifyEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var binding: ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        verifyEmail(binding.myEmailTextLogin)
        verifyLoginFields()
    }

    private fun verifyLoginFields(){
        val email = binding.myEmailTextLogin
        val password = binding.myPasswordTextLogin

        binding.buttonLogin.setOnClickListener {
           val userEmailValue = email.text.toString()
           val userPassword = password.text.toString()
            var canLogin = true


            if(userEmailValue.isEmpty() || email.error != null){
                binding.myEmailTextLogin.error = "Email should be filled"
                canLogin = false
            }
            if(userPassword.isEmpty() || password.error != null){
                binding.myPasswordTextLogin.error = "Password should be filled"
                canLogin = false
            }
            if(canLogin){
                loginUser(userEmailValue, userPassword)
            }
        }
    }

    private fun loginUser(email: String, password: String){
        showLoading(true)
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.isSuccessful){
                    showLoading(false)
                    val responseBody = response.body()

                    if(responseBody != null){
                        Toast.makeText(baseContext, responseBody.message, Toast.LENGTH_SHORT).show()
                        saveToken(
                            responseBody.loginResult?.token.toString(),
                            responseBody.loginResult?.name.toString(),
                            email
                        )

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                        Log.e(TAG, "User LoggedIn Success: ${responseBody.loginResult?.token}")
                    }else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        showLoading(false)
                    }
                }else{
                    showLoading(false)
                    Toast.makeText(baseContext, "your password or email is wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                showLoading(false)

            }

        })

    }


    private fun saveToken(token: String, name: String, email: String){
        val preferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        preferences.edit().putString("token", token).apply()
        preferences.edit().putString("name", name).apply()
        preferences.edit().putString("email", email).apply()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}