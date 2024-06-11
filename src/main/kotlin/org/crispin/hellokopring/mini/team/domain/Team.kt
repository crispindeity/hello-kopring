package org.crispin.hellokopring.mini.team.domain

class Team private constructor(
    var id: Long? = null,
    val name: String,
    var managerId: Long? = null,
) {
    companion object {
        fun createTeam(id: Long? = null, name: String): Team {
            return Team(id, name, null)
        }
    }
    fun modifyManager(employeeId: Long?) {
        this.managerId = employeeId
    }
}
