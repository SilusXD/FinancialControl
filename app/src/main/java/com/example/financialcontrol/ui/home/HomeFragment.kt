package com.example.financialcontrol.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
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
import android.widget.TimePicker
import androidx.core.view.get
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import com.example.financialcontrol.DatabaseHelper
import com.example.financialcontrol.R
import com.example.financialcontrol.Transaction
import com.example.financialcontrol.TransactionAdapter
import com.example.financialcontrol.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var homeViewModel: HomeViewModel? = null
    private var listViewTransactions: ListView? = null
    private var buttonAdd: Button? = null
    private var editTextDate: EditText? = null
    private var checkBoxWithdraw: CheckBox? = null
    private var checkBoxAllTransactions: CheckBox? = null
    private var outlineEditTextAmountMoney: TextInputLayout? = null
    private var dateOfTransactions: LocalDate = LocalDate.now()

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
        editTextDate = binding.editTextDate
        checkBoxWithdraw = binding.checkBoxWithdraw
        checkBoxAllTransactions = binding.checkBoxAllTransactions
        outlineEditTextAmountMoney = binding.outlineEditTextAmountMoney


        buttonAdd!!.setOnClickListener()
        {
            if(outlineEditTextAmountMoney!!.editText?.text?.isNotEmpty() == true)
            {
                val amountMoney = outlineEditTextAmountMoney!!.editText?.text.toString().toDouble()

                if(amountMoney != 0.0)
                {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Filling")
                    val timePicker = TimePicker(context)
                    builder.setView(timePicker)

                    builder.setPositiveButton("OK",
                        DialogInterface.OnClickListener{
                                dialog,
                                which ->
                            var time: LocalTime
                            try
                            {
                                val hourStr: String
                                val minuteStr: String

                                if(timePicker.hour < 10)
                                    hourStr = "0$timePicker.hour"
                                else
                                    hourStr = timePicker.hour.toString()

                                if(timePicker.minute < 10)
                                    minuteStr = "0${timePicker.minute + 1}"
                                else
                                    minuteStr = (timePicker.minute + 1).toString()
                                time = LocalTime.parse("${hourStr}:${minuteStr}",
                                    DateTimeFormatter.ofPattern("HH:mm"))
                            }
                            catch (ex: DateTimeParseException)
                            {
                                time = LocalTime.now()
                                ex.printStackTrace()
                            }
                            if(checkBoxWithdraw!!.isChecked)
                            {
                                val db = activity?.let { DatabaseHelper(it.applicationContext, null) }

                                db?.addTransaction(LocalDateTime.of(dateOfTransactions, time), amountMoney, 1)
                                updateListViewTransactions(checkBoxAllTransactions!!.isChecked)
                            }
                            else
                            {
                                val db = activity?.let { DatabaseHelper(it.applicationContext, null) }

                                db?.addTransaction(LocalDateTime.of(dateOfTransactions, time), amountMoney, 0)
                                updateListViewTransactions(checkBoxAllTransactions!!.isChecked)
                            }
                        })
                    builder.setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                    builder.show()
                }
            }
        }

        editTextDate!!.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    val dayOfMonthStr: String
                    val monthOfYearStr: String

                    if(dayOfMonth < 10)
                        dayOfMonthStr = "0$dayOfMonth"
                    else
                        dayOfMonthStr = dayOfMonth.toString()

                    if(monthOfYear < 10)
                        monthOfYearStr = "0${monthOfYear + 1}"
                    else
                        monthOfYearStr = (monthOfYear + 1).toString()
                    val date = ("$dayOfMonthStr.$monthOfYearStr.$year")
                    editTextDate?.setText(date)
                    dateOfTransactions = LocalDate.parse(
                        date,
                        DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    checkBoxAllTransactions!!.isChecked = false
                    updateListViewTransactions(checkBoxAllTransactions!!.isChecked)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        checkBoxAllTransactions!!.setOnClickListener{
            updateListViewTransactions(checkBoxAllTransactions!!.isChecked)
        }

        listViewTransactions!!.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position)

            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Delete?")
            builder.setPositiveButton("OK",
                DialogInterface.OnClickListener{
                        dialog,
                        which ->
                    val db = activity?.let { DatabaseHelper(it.applicationContext, null) }
                    var record: Transaction
                    homeViewModel!!.getTransactions().observe(viewLifecycleOwner)
                    { it ->
                        try
                        {
                            val filteredList = it.filter {
                                it.datetime.toLocalDate() == dateOfTransactions && it.isDeleted == 0 }
                            if (position in filteredList.indices) {
                                record = filteredList[position]
                                db?.setIsDeletedTransaction(record.id)
                            }
                        }
                        catch (ex: IndexOutOfBoundsException)
                        {
                            ex.printStackTrace()
                        }
                    }
                    updateListViewTransactions()
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builder.show()


        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        editTextDate?.setText(dateOfTransactions.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy")))
        updateListViewTransactions(checkBoxAllTransactions!!.isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateListViewTransactions(all: Boolean = true)
    {
        val db = activity?.let { DatabaseHelper(it.applicationContext, null) }
        val listTransactions: MutableList<Transaction> = mutableListOf()

        val cursor = db?.getTransactions()
        if(cursor?.moveToFirst() == false)
        {
            return
        }
        do
        {
            if(cursor == null)
            {
                return
            }
            val transaction: Transaction = Transaction(
                cursor.getInt(0),
                LocalDateTime.parse(cursor.getString(1)),
                cursor.getDouble(2),
                cursor.getInt(3),
                cursor.getInt(4),
            )

            listTransactions.add(transaction)
        }
        while (cursor?.moveToNext() == true)

        homeViewModel!!.setTransactions(listTransactions)

        homeViewModel!!.getTransactions().observe(viewLifecycleOwner)
        {
            val adapter: TransactionAdapter?
            if(all)
            {
                adapter = activity?.applicationContext?.let {
                        it1 -> TransactionAdapter(it1, it.filter { it.isDeleted == 0 })
                }
            }
            else
            {
                adapter = activity?.applicationContext?.let {
                        it1 -> TransactionAdapter(it1, it.filter {
                    it.datetime.toLocalDate() == dateOfTransactions && it.isDeleted == 0
                })
                }
            }

            listViewTransactions!!.adapter = adapter
        }
    }
}