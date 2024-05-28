package org.crispin.hellokopring.mini.employee.domain

import java.time.LocalDate

data class Employee(
    val id: Long? = null,
    val name: String,
    val teamId: Long? = null,
    var isManager: Boolean = false,
    val enteringDate: LocalDate,
    val birthday: LocalDate,
)
