package org.crispin.hellokopring.mini.team.domain

data class Team(
    var id: Long? = null,
    val name: String,
    var managerId: Long? = null,
) {
    fun modifyManager(employeeId: Long) {
        this.managerId = employeeId
    }
}
