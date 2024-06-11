package org.crispin.hellokopring.mini.team.service

import org.crispin.hellokopring.mini.team.domain.Team
import org.crispin.hellokopring.mini.team.repository.TeamRepository

class TeamService(
    private val teamRepository: TeamRepository,
) {

    fun register(team: Team): Team {
        return teamRepository.save(team)
    }

    fun retrieveAll(): List<Team> {
        return teamRepository.findAll()
    }
}
