package org.crispin.hellokopring.mini.team.repository

import org.crispin.hellokopring.mini.team.domain.Team

interface TeamRepository {
    fun save(team: Team): Team
    fun findAll(): List<Team>
    fun findById(id: Long): Team?
}
