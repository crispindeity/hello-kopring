package org.crispin.hellokopring.mock

import org.crispin.hellokopring.mini.team.domain.Team
import org.crispin.hellokopring.mini.team.repository.TeamRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TeamFakeRepository : TeamRepository {

    private val storage: MutableMap<Long, Team> = ConcurrentHashMap()
    private var sequence: AtomicLong = AtomicLong(1L)

    override fun save(team: Team): Team {
        val id = team.id ?: sequence.getAndIncrement()
        storage[id] = team.copy(id = id)
        return storage[id]!!
    }

    override fun findAll(): List<Team> {
        return storage.values.toList()
    }

    override fun findById(id: Long): Team? {
        return storage.values.find { it.id == id }
    }
}
