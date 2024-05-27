package org.crispin.hellokopring.day02.controller

import java.time.LocalDate

data class FruitInfoRequest(
    val name: String,
    val warehousingDate: LocalDate,
    val price: Long,
)
