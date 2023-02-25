package com.anushka.roomdemo

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anushka.roomdemo.db.Subscriber
import com.anushka.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubscriberViewModel(private val repository:SubscriberRepository) : ViewModel() {
    val subscribers = repository.subscribers

    private var isUpdateOrDelete : Boolean = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()

    val saveorUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
        get() = statusMessage


    init{
        saveorUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate(){
        if(inputName.value==null){
            statusMessage.value = Event("Please enter subscriber's name")
        }else if(inputEmail.value==null){
            statusMessage.value = Event("Please enter subscriber's email")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please enter a correct email address")
        }else{
            if(isUpdateOrDelete){
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputName.value!!
                update(subscriberToUpdateOrDelete)
            }else{
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0,name,email))
                inputName.value = ""
                inputEmail.value= ""
            }
        }
    }
    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber)= viewModelScope.launch(Dispatchers.IO){
            var newRowId = repository.insert(subscriber)
        withContext(Dispatchers.Main){
            if(newRowId>-1){
                statusMessage.value = Event("Subscriber Inserted Successfully! $newRowId")
            }else{
                statusMessage.value = Event("Error Occurred !")
            }
        }
    }
    private fun update(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.update(subscriber)
        withContext(Dispatchers.Main){
            if(numberOfRows>0){
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveorUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("Subscriber Updated Successfully !")
            }else{
                statusMessage.value = Event("Error Occurred !")
            }
        }
    }
    private fun delete(subscriber: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.delete(subscriber)
        withContext(Dispatchers.Main){
            if(numberOfRows>0){
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete = false
                saveorUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value = "Clear All"
                statusMessage.value = Event("Subscriber Deleted Successfully !")
            }
            else{
            statusMessage.value = Event("Error Occurred !")
            }
        }


    }
    private fun clearAll() = viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.deleteAll()
        withContext(Dispatchers.Main){
            if(numberOfRows>0){
                statusMessage.value = Event("All Subscriber Deleted Successfully !")
            }else{
                statusMessage.value = Event("Error Occured!")
            }
        }
    }
    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveorUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}