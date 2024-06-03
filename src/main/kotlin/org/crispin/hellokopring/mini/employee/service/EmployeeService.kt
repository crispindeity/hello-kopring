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
        verifyManagerRegisterEligibility(employee)
        return employeeRepository.save(employee)
    }

    fun retrieveAll(): List<Employee> {
        return employeeRepository.findAll()
    }

    fun modifyPosition(id: Long) {
        val employee = employeeRepository.findById(id)
            ?: throw IllegalArgumentException("등록되지 않은 직원 아이디입니다. $id")

        require(employee.teamId != null) {
            "팀에 등록되지 않은 직원입니다. ${employee.id}"
        }

        val team = teamRepository.findById(employee.teamId)!!

        team.modifyManager(employee.id!!)
        teamRepository.save(team)

        employee.modifyPosition()
        employeeRepository.save(employee)
    }

    private fun verifyTeamId(employee: Employee) {
        employee.teamId?.run {
            teamRepository.findById(this)
                ?: throw IllegalArgumentException("등록되지 않은 팀 아이디 입니다. ${employee.teamId}")
        }
    }

    private fun verifyManagerRegisterEligibility(employee: Employee) {
        when {
            employee.teamId == null && employee.isManager -> {
                throw IllegalArgumentException("팀에 등록되지 않은 멤버는 매니저로 설정할 수 없습니다. ${employee.id}")
            }
        }
    }
}
