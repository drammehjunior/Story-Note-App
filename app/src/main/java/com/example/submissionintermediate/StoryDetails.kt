package com.example.submissionintermediate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.submissionintermediate.databinding.ActivityStoryDetailsBinding

class StoryDetails : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")!!
        val description = intent.getStringExtra("description")!!
        val photo = intent.getStringExtra("photo")!!

        setUpView(name, description, photo)

    }

    fun setUpView(name: String, description: String, photo: String){
        binding.userName.text = name
        Glide.with(this)
            .load(photo)
            .into(binding.imageDetail)
        binding.storyDescription.text = description

    }
}