package com.example.financialcontrol.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel()
{
    private val _listTransactions = MutableLiveData<List<String>>().apply {
        value = listOf(
            "Virat Kohli", "Rohit Sharma", "Steve Smith",
            "Kane Williamson", "Ross Taylor"
        )
    }
    val listTransactions: LiveData<List<String>> = _listTransactions
}