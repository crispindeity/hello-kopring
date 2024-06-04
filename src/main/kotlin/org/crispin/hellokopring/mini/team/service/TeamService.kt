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

    fun managerRegister(teamId: Long, employeeId: Long): Team {
        teamRepository.findById(teamId)?.run {
            val verifiedEmployee = verifyManagerRegisterEligibility(employeeId, this)
            verifiedEmployee.modifyPosition()

            this.managerId?.run {
                modifyOldManager(this)
            }

            employeeRepository.save(verifiedEmployee)

            this.modifyManager(employeeId)
            register(this)

            return this
        } ?: throw IllegalArgumentException("존재하지 않는 팀 아이디 입니다. $teamId")
    }

    private fun verifyManagerRegisterEligibility(team: Team) {
        team.managerId?.run {
            val employee: Employee? = employeeRepository.findById(this)
            require(employee != null && employee.teamId == team.id) {
                "등록되지 않은 직원 아이디 입니다. ${team.managerId}"
            }
        }
    }

    private fun verifyManagerRegisterEligibility(employeeId: Long, team: Team): Employee {
        employeeRepository.findById(employeeId)?.run {
            require(this.teamId != null || this.teamId == team.id) {
                "팀에 등록되지 않은 직원 아이디 입니다. ${this.id}"
            }
            return this
        } ?: throw IllegalArgumentException("존재하지 않는 직원 아이디 입니다. $employeeId")
    }

    private fun modifyOldManager(id: Long) {
        employeeRepository.findById(id)?.run {
            this.modifyPosition()
            employeeRepository.save(this)
        }
    }
}
