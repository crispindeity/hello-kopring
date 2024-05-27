package org.crispin.hellokopring.mini.employee.domain

import java.time.LocalDate

data class Employee(
    val id: Long,
    val name: String,
    var isManager: Boolean,
    val enteringDate: LocalDate,
    val birthday: LocalDate,
)
