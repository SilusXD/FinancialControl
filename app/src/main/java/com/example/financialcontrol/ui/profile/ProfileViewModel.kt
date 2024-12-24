package com.example.financialcontrol.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialcontrol.User

class ProfileViewModel : ViewModel()
{
    private val _user = MutableLiveData<User>()
    fun setUser(input: User)
    {
        _user.value = input
    }
    fun getUser(): MutableLiveData<User> = _user
}