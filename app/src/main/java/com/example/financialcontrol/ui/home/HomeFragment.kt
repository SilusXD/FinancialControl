package com.example.financialcontrol.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import com.example.financialcontrol.R
import com.example.financialcontrol.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputLayout

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var homeViewModel: HomeViewModel? = null
    private var listViewTransactions: ListView? = null
    private var buttonAdd: Button? = null
    private var buttonDelete: Button? = null
    private var checkBoxWithdraw: CheckBox? = null
    private var outlineEditTextAmountMoney: TextInputLayout? = null
    private val tempListTransactions: MutableList<Double> = mutableListOf(12000.0, 1000.0, 52000.0, 9999.99)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listViewTransactions = binding.listViewTransactions
        buttonAdd = binding.buttonAdd
        buttonDelete = binding.buttonDelete
        checkBoxWithdraw = binding.checkBoxWithdraw
        outlineEditTextAmountMoney = binding.outlineEditTextAmountMoney


        homeViewModel!!.setTransactions(tempListTransactions)


        buttonAdd!!.setOnClickListener()
        {
            if(outlineEditTextAmountMoney!!.isNotEmpty())
            {
                val amountMoney = outlineEditTextAmountMoney!!.editText?.text.toString().toDouble()

                if(amountMoney != 0.0)
                {
                    if(checkBoxWithdraw!!.isChecked)
                    {
                        addTransaction(amountMoney)
                        updateListViewTransactions()
                    }
                    else
                    {
                        addTransaction(amountMoney)
                        updateListViewTransactions()
                    }
                }
            }
        }

        buttonDelete!!.setOnClickListener()
        {

        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        updateListViewTransactions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addTransaction(amountMoney: Double)
    {
        tempListTransactions.add(amountMoney)
    }

    private fun updateListViewTransactions()
    {
        homeViewModel!!.getTransactions().observe(viewLifecycleOwner)
        {
            val adapter = activity?.applicationContext?.let {
                    it1 -> ArrayAdapter(it1, android.R.layout.simple_list_item_1, it)
            }

            listViewTransactions!!.adapter = adapter
        }
    }
}