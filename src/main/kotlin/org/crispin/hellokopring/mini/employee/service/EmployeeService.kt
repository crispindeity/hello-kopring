package org.crispin.hellokopring.mini.employee.service

import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository
import org.crispin.hellokopring.mini.team.repository.TeamRepository

class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    private val teamRepository: TeamRepository
) {
    fun register(employee: Employee): Employee {
        verifyTeamId(employee)
        return employeeRepository.save(employee)
    }

    fun retrieveAll(): List<Employee> {
        return employeeRepository.findAll()
    }

    private fun verifyTeamId(employee: Employee) {
        employee.teamId?.run {
            teamRepository.findById(this)
                ?: throw IllegalArgumentException("등록되지 않은 팀 아이디 입니다. ${employee.teamId}")
        }
    }
}
