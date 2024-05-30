package org.crispin.hellokopring.mini.employee.repository

import org.crispin.hellokopring.mini.employee.domain.Employee

interface EmployeeRepository {
    fun save(employee: Employee): Employee
    fun findAll(): List<Employee>
    fun findById(id: Long): Employee?
}
