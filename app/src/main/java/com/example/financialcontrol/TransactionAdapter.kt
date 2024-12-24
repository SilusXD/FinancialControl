package com.example.financialcontrol

import android.content.Context
import android.content.res.Configuration
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

        val currentNightMode: Int = context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_NO)
        {
            datetimeView.setTextColor(Color.BLACK)
            amountView.setTextColor(Color.BLACK)
        }
        else if(currentNightMode == Configuration.UI_MODE_NIGHT_YES)
        {
            datetimeView.setTextColor(Color.WHITE)
            amountView.setTextColor(Color.WHITE)
        }

        val transaction: Transaction = transactions[position]

        amountView.text = transaction.amount.toString()

        val db = DatabaseHelper(context, null)
        val type = db.selectTypeOfTransactionInTypesOfTransactionsById(transaction.idType)
        typeView.text = type
        if(type == "Deposit")
        {
            typeView.setTextColor(Color.GREEN)
        }
        else
        {
            typeView.setTextColor(Color.RED)

        }
        datetimeView.text = transaction.datetime.format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        return view
    }
}