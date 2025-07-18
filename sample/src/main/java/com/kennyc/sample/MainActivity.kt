package com.kennyc.sample

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kennyc.sample.databinding.ActivityMainBinding
import com.kennyc.sample.databinding.EmptyViewBinding
import com.kennyc.sample.databinding.ErrorViewBinding
import com.kennyc.view.MultiStateView
import kotlin.random.Random

class MainActivity : AppCompatActivity(), MultiStateView.StateListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.multiStateView.listener = this
//        multiStateView.getView(MultiStateView.ViewState.ERROR)?.findViewById<Button>(R.id.retry)
//                ?.setOnClickListener {
//                    multiStateView.viewState = MultiStateView.ViewState.LOADING
//                    Toast.makeText(applicationContext, "Fetching Data", Toast.LENGTH_SHORT).show()
//                    multiStateView.postDelayed({ multiStateView.viewState = MultiStateView.ViewState.CONTENT }, 3000L)
//                }


        val data = arrayOfNulls<String>(100)
        for (i in 0..99) {
            data[i] = "Row $i"
        }

        binding.list.adapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.error -> {
                with(binding.multiStateView) {
                    viewState = MultiStateView.ViewState.ERROR
                    getViewBinding(MultiStateView.ViewState.ERROR, ErrorViewBinding::class.java)?.apply {
                        retry.setOnClickListener {
                            viewState = MultiStateView.ViewState.LOADING
                            Toast.makeText(applicationContext, "Fetching Data", Toast.LENGTH_SHORT).show()
                            postDelayed({ viewState = MultiStateView.ViewState.CONTENT }, 3000L)
                        }
                    }
                }
                return true
            }

            R.id.empty -> {
                with(binding.multiStateView) {
                    viewState = MultiStateView.ViewState.EMPTY
                    getViewBinding(MultiStateView.ViewState.EMPTY, EmptyViewBinding::class.java)?.apply {
                        root.text = "No Result code ${Random.nextInt()}"
                    }
                }
                return true
            }

            R.id.content -> {
                binding.multiStateView.viewState = MultiStateView.ViewState.CONTENT
                return true
            }

            R.id.loading -> {
                binding.multiStateView.viewState = MultiStateView.ViewState.LOADING
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStateChanged(viewState: MultiStateView.ViewState) {
        Log.v("MSVSample", "onStateChanged; viewState: $viewState")
    }
}