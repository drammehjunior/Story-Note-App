package com.example.submissionintermediate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermediate.Api.ApiConfig
import com.example.submissionintermediate.databinding.ActivityMainBinding
import com.example.submissionintermediate.pojo.ListStoryItem
import com.example.submissionintermediate.pojo.ResponseGetAllStory
import retrofit2.Response


private var tokenGet: String = ""


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onStart() {
        super.onStart()
        checkLoggedIn()
    }

    private val mainViewModel  by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkLoggedIn()

        if(intent.getBooleanExtra("reload", false)){
            mainViewModel.findStories()
        }

        val preferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val name = preferences.getString("name", null)
        binding.welcomeMessage.text = "Welcome back ${name}"

        mainViewModel.listStories.observe(this) { listStories ->
            setStories(listStories)
        }

        mainViewModel.isLoading.observe(this){ it ->
            showLoading(it)
        }

        mainViewModel.findStories()

        binding.fabAdd.setOnClickListener { view ->
            if(view.id == R.id.fab_add){
                val intent = Intent(this@MainActivity, CameraActivity::class.java)
                intent.putExtra("token", tokenGet)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menu1 -> {
                logUserOut()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        finishAffinity()
    }


    @SuppressLint("ApplySharedPref")
    private fun logUserOut(){
        val preferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        preferences.edit().clear().commit()

        startActivity(Intent(this@MainActivity, welcomeActivity::class.java))
        finish()
    }

    private fun checkLoggedIn(){
        val preferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val token = preferences.getString("token", null)
        if(token == null){
            startActivity(Intent(this@MainActivity, welcomeActivity::class.java))
            finish()
        }else{
            tokenGet = token.toString()
            mainViewModel.TokenView = token.toString()
        }
    }

    private fun setStories(listUsers: List<ListStoryItem>){
        if(applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.rvStories.layoutManager = GridLayoutManager(this, 2)
        }else{
            binding.rvStories.layoutManager = LinearLayoutManager((this))
        }
        val listStoriesAdapter = StoriesAdapter(listUsers)
        listStoriesAdapter.setOnItemClickCallback(object: StoriesAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListStoryItem) {
                Toast.makeText(this@MainActivity, "Hello", Toast.LENGTH_SHORT).show()
                val intentToDetail = Intent(this@MainActivity, StoryDetails::class.java)
                intentToDetail.putExtra("name", data.name)
                intentToDetail.putExtra("photo", data.photoUrl)
                intentToDetail.putExtra("description", data.description)
                startActivity(intentToDetail)
            }
        })

        binding.rvStories.adapter = listStoriesAdapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}

class MainViewModel(): ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var TokenView = ""


    companion object{
        private const val TAG = "MainViewModel"
    }

      fun findStories(){
          _isLoading.value = true
        Log.e(TAG, TokenView)

         val client = ApiConfig.getApiService().getStories("Bearer $TokenView")
            client.enqueue(object : retrofit2.Callback<ResponseGetAllStory>{
                override fun onResponse(
                    call: retrofit2.Call<ResponseGetAllStory>,
                    response: Response<ResponseGetAllStory>
                ) {

                    if(response.isSuccessful){
                        _isLoading.value = false

                        val responseBody = response.body()
                        Log.e(TAG, responseBody?.listStory?.get(4)?.name.toString())
                        if(responseBody != null){
                            _listStories.value = responseBody.listStory!!
                        }

                    }
                }

                override fun onFailure(call: retrofit2.Call<ResponseGetAllStory>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "IT IS FAILED")
                }

            })
    }

}
