package com.wizeline.heroes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.wizeline.heroes.databinding.ActivityMainBinding
import com.wizeline.heroes.ui.fragments.CharactersFragment
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val privateKey = "244be7c2496cbf6d331145cc489b4892457cc2c0"
    private val apikey = "6ffcf49b680b7250a6983acd33731f55"
    private val ts = System.currentTimeMillis().toString()

    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        requestCharacters()
    }

    private fun setupView() {
        supportFragmentManager.beginTransaction().add(R.id.container , CharactersFragment.newInstance()).commit()
    }

    private fun requestCharacters() {
        val hash = (ts + privateKey + apikey).toMD5()

        // Use disposable to prevent observable memory leaks

        compositeDisposable.add(
            NetworkClient.getServices().characters(ts, apikey, hash, 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response -> onResponse(response)},{error -> onError(error)})
        )
        /**NetworkClient.getServices().characters(ts, apikey, hash, 10)
        .enqueue(object : retrofit2.Callback<Characters> {
        override fun onResponse(call: Call<Characters>, response: Response<Characters>) {
        response.body()?.data?.results?.forEach {
        Log.i("requestCharacters", "${it.name}")
        }
        }

        override fun onFailure(call: Call<Characters>, t: Throwable) {
        t.printStackTrace()
        Log.i("requestCharacters", call.toString())
        }
        })
         **/
    }

    private fun onResponse(response: Characters?) {
        if (response != null){
            response.data.results.forEach {
                Log.i("requestCharacters", it.name)
            }
        }
    }

    private fun onError(error: Throwable?) {
        error?.printStackTrace()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
