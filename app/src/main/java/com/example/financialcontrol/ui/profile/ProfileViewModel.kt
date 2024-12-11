package com.example.financialcontrol.ui.profile

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialcontrol.DatabaseHelper
import com.example.financialcontrol.R

class ProfileViewModel : ViewModel()
{
    private val _name = MutableLiveData<String>()
    fun setName(input: String)
    {
        _name.value = input
    }
    fun getName(): MutableLiveData<String> = _name


    private val _email = MutableLiveData<String>()
    fun setEmail(input: String)
    {
        _email.value = input
    }
    fun getEmail(): MutableLiveData<String> = _email
}