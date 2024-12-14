package com.example.financialcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel()
{
    private val _listTransactions = MutableLiveData<List<Double>>()

    fun setTransactions(input: List<Double>)
    {
        _listTransactions.value = input
    }
    fun getTransactions(): MutableLiveData<List<Double>> = _listTransactions
}