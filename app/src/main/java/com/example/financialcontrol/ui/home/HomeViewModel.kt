package com.example.financialcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.financialcontrol.Transaction

class HomeViewModel : ViewModel()
{
    private val _listTransactions = MutableLiveData<List<Transaction>>()

    fun setTransactions(input: List<Transaction>)
    {
        _listTransactions.value = input
    }
    fun getTransactions(): MutableLiveData<List<Transaction>> = _listTransactions
}