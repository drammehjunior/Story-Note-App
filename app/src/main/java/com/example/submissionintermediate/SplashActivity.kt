package com.example.submissionintermediate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // langsung pindah ke MainActivity atau activity lain
        // begitu memasuki splash screen ini

        GlobalScope.launch(Dispatchers.Main){
            delay(1000)
            val intent = Intent(this@SplashActivity, welcomeActivity::class.java)
            startActivity(intent)
            finish()
        }



    }
}