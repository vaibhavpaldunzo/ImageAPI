package com.example.imageapi

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"

    private var searchView : SearchView? = null

    private var imageWindow : ImageView? = null

    private lateinit var db : AppDatabase

    var context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getAppDatabase(MyApplication.myApplicationContext!!)

        Log.v(TAG, db.toString())

        searchView = findViewById(R.id.searchView)

        imageWindow = findViewById(R.id.imageWindow)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                var queryWord = query

               /* val urlList : List<String> = stringToList(getImages(queryWord))

                imageWindow?.let { imageView ->
                    Glide.with(applicationContext).load(urlList[0]).into(imageView)
                }*/

                getImages(queryWord) { urlList ->

                    Log.v(TAG, "inside runAsync")
                    Log.v(TAG, urlList.toString())

                    imageWindow?.let { imageView ->
                        val urlItems: List<String> = Gson().fromJson(urlList[0], object : TypeToken<List<String>> () {}.type)
                        Glide.with(applicationContext).load(urlItems[0]).into(imageView)
                    }
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        //getImages("dog")
    }

    private fun getImages(queryWord: String, runAsync: (List<String>) -> Unit){
        var result = db.imageListDao().findByWord(queryWord)

        if (result.isEmpty()) {
            var retrofit = RetrofitInstance.retrofitInstance

            var getImageService = retrofit?.create(ImageApiService::class.java)
            getImageService?.let {
                var call : Call<SearchResponse> = it.getImages(queryWord)

                call.enqueue(object : Callback<SearchResponse?> {
                    override fun onResponse(
                        call: Call<SearchResponse?>?,
                        response: Response<SearchResponse?>
                    ) {

                        if (response.code() == 200) {

                            var responseData = response.body()

                            if (responseData!!.data.isEmpty()) {
                                errorToast()
                            } else {

                                result = convertToListOfStrings(responseData.data)

                                db.imageListDao().insert(ImageList(0, queryWord, result))

                                runAsync(result)

                            }

                        } else {

                            Log.v(TAG, "Code fatt gya")

                            errorToast()

                        }

                    }

                    override fun onFailure(
                        call: Call<SearchResponse?>,
                        t: Throwable
                    ) {
                        errorToast()
                    }
                })
            }


        }else{
            Log.v(TAG, "DB ka result -> ${result.toString()}")

            runAsync(result)
        }
    }


    private fun convertToListOfStrings(list : List<Data>) : List<String> {
        val res = mutableListOf<String>()

        list.forEach {
            res.add(it.imageurl.toString())
        }

        return res;
    }

    private fun stringToList(str : String) : List<String> {

        val listType = object : TypeToken<List<String>>() {}.type

        return Gson().fromJson(str, listType)

    }

    private fun errorToast() {
        Toast.makeText(
            this@MainActivity,
            "Something went wrong...Sorry!!",
            Toast.LENGTH_SHORT
        ).show()
    }

//    fun runAsync(callback: (List<String>) -> Unit){
//
//    }


}
