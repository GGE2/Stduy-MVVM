package com.anushka.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anushka.roomdemo.databinding.ActivityMainBinding
import com.anushka.roomdemo.db.Subscriber
import com.anushka.roomdemo.db.SubscriberDatabase
import com.anushka.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter: RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(applicationContext).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this,factory)[SubscriberViewModel::class.java]
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter { selectedItem: Subscriber -> listItemClicked(selectedItem) }
        binding.subscriberRecyclerView.adapter = adapter
        displaySubScribersList()
    }
    private fun displaySubScribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("MyTag",it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }
    private fun listItemClicked(subscriber: Subscriber){
        Toast.makeText(this,"selected name is${subscriber.name}",Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }

}