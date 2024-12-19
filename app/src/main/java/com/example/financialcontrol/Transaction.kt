package com.example.financialcontrol

import java.time.LocalDateTime

data class Transaction(var id: Int, var datetime: LocalDateTime,
                       var amount: Double, var idType: Int, var isDeleted: Int)
