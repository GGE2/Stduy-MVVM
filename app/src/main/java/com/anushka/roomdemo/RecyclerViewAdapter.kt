package com.anushka.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anushka.roomdemo.databinding.ListItemBinding
import com.anushka.roomdemo.db.Subscriber

class RecyclerViewAdapter(private val clickListener:(Subscriber)->Unit)
    : RecyclerView.Adapter<RecyclerViewAdapter.MyHolder>(){
    private val subscribersList = ArrayList<Subscriber>()

    class MyHolder(val binding:ListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(subscriber: Subscriber,clickListener:(Subscriber)->Unit){
            binding.nameTextView.text = subscriber.name
            binding.nameTextEmail.text = subscriber.email
            binding.listItemLayout.setOnClickListener {
                clickListener(subscriber)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ListItemBinding>(layoutInflater,R.layout.list_item,parent,false)

        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(subscribersList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return subscribersList.size
    }
    fun setList(subscribers: List<Subscriber>){
        subscribersList.clear()
        subscribersList.addAll(subscribers)
    }
}