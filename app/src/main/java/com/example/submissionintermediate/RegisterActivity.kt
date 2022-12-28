package com.example.submissionintermediate

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.submissionintermediate.Api.ApiConfig
import com.example.submissionintermediate.databinding.ActivityRegisterBinding
import com.example.submissionintermediate.pojo.ResponseLogin
import com.example.submissionintermediate.pojo.ResponseRegister
import com.example.submissionintermediate.utils.verifyEmail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private lateinit var binding: ActivityRegisterBinding




class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        verifyEmail(binding.myEmailText)
        verifyAllFields()
    }



    private fun verifyAllFields(){
        val userName = binding.myEditText
        val userEmail = binding.myEmailText
        val userPassword = binding.myPasswordText

        binding.buttonRegister.setOnClickListener {
            val userNameValue = binding.myEditText.text.toString()
            val userEmailValue = binding.myEmailText.text.toString()
            val userPasswordValue = binding.myPasswordText.text.toString()

            if(userNameValue.isEmpty()){
                binding.myEditText.error = "Name should be filled"
            }
            if(userEmailValue.isEmpty()){
                userEmail.error = "Email should be filled"
            }
            if(userPasswordValue.isEmpty()){
                userPassword.error = "Password should be filled"
            }

            if(userName.error != null || userEmail.error != null || userPassword.error != null){
                Toast.makeText(this, "It Failed" ,Toast.LENGTH_SHORT).show()
            }else{
                registerUser(userNameValue, userEmailValue, userPasswordValue)
            }

        }
    }

    private fun registerUser(userName: String, userEmail: String, userPassword: String){
        showLoading(true)
        val client = ApiConfig.getApiService().signupUser(userName, userEmail, userPassword)
        client.enqueue(object : Callback<ResponseRegister>{
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if(response.isSuccessful){
                    showLoading(false)
                    val responseBody = response.body()
                    if(responseBody != null){
                        Log.e(TAG, "User Create Success: ${responseBody.message}")
                        loginUser(userEmail, userPassword)
                    }
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showLoading(false)
                    binding.myEmailText.error = "Email already taken, try another"
                    Toast.makeText(baseContext, "The email is chosen already", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                showLoading(false)
            }
        })
    }

    private fun loginUser(email: String, password: String){
        showLoading(true)
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<ResponseLogin>{
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
                        Log.e(TAG, "User Create Success: ${responseBody.loginResult?.token}")
                    }else {
                        showLoading(false)
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
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



