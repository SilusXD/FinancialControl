package com.example.financialcontrol

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.color.MaterialColors.getColor
import java.time.format.DateTimeFormatter


class TransactionAdapter(context: Context, transactions: List<Transaction>) :
    ArrayAdapter<Transaction?>(context, R.layout.list_item, transactions)
{
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val transactions: List<Transaction> = transactions

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val view = inflater.inflate(R.layout.list_item, parent, false)

        val amountView = view.findViewById<TextView>(R.id.amount)
        val typeView = view.findViewById<TextView>(R.id.type)
        val datetimeView = view.findViewById<TextView>(R.id.datetime)

        val transaction: Transaction = transactions[position]

        amountView.text = transaction.amount.toString()

        if(transaction.idType == 0)
        {
            typeView.text = "Приход"
            typeView.setTextColor(Color.GREEN)
        }
        else
        {
            typeView.text = "Расход"
            typeView.setTextColor(Color.RED)

        }

        datetimeView.text = transaction.datetime.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        return view
    }
}