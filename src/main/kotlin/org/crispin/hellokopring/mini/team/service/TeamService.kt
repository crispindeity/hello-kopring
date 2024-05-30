package org.crispin.hellokopring.mini.team.service

import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository
import org.crispin.hellokopring.mini.team.domain.Team
import org.crispin.hellokopring.mini.team.repository.TeamRepository

class TeamService(
    private val teamRepository: TeamRepository,
    private val employeeRepository: EmployeeRepository
) {

    fun register(team: Team): Team {
        verifyManagerRegisterEligibility(team)
        return teamRepository.save(team)
    }

    fun retrieveAll(): List<Team> {
        return teamRepository.findAll()
    }

    private fun verifyManagerRegisterEligibility(team: Team) {
        team.managerId?.run {
            val employee: Employee? = employeeRepository.findById(this)
            require(employee != null && employee.teamId == team.id) {
                "등록되지 않은 직원 아이디 입니다. ${team.managerId}"
            }
        }
    }
}
