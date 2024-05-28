package org.crispin.hellokopring.mini.employee.service

import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository

class EmployeeService(
    private val employeeRepository: EmployeeRepository
) {
    fun register(employee: Employee): Employee {
        return employeeRepository.save(employee)
    }

    fun retrieveAll(): List<Employee> {
        return employeeRepository.findAll()
    }
}
