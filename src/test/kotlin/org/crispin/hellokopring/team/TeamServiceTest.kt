package org.crispin.hellokopring.team

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class TeamServiceTest : DescribeSpec({

    data class Team(
        var id: Long? = null,
        val name: String,
    )

    class TeamFakeRepository {
        val storage: MutableMap<Long, Team> = ConcurrentHashMap()
        var sequence: AtomicLong = AtomicLong(1L)


        fun save(team: Team): Team {
            val id = team.id ?: sequence.getAndIncrement()
            storage[id] = team.copy(id = id)
            return storage[id]!!
        }

        fun findAll(): List<Team> {
            return storage.values.toList()
        }

    }

    class TeamService(
        val teamRepository: TeamFakeRepository
    ) {
        fun register(team: Team): Team {
            return teamRepository.save(team)
        }

        fun retrieveAll(): List<Team> {
            return teamRepository.findAll()
        }
    }

    lateinit var teamService: TeamService
    lateinit var teamRepository: TeamFakeRepository

    beforeTest {
        teamRepository = TeamFakeRepository()
        teamService = TeamService(teamRepository)
    }

    describe("팀 서비스 테스트") {

        describe("팀 등록 테스트") {

            describe("팀 등록 성공 테스트") {

                it("정상 적인 팀 정보 입력 시 등록에 성공 해야 한다.") {
                    // given
                    val team = Team(name = "테스트 1팀")

                    // when
                    val registeredTeam: Team = teamService.register(team)

                    // then
                    registeredTeam.id shouldBe 1L
                }
            }
        }

        describe("팀 조회 테스트") {

            describe("팀 조회 성공 테스트") {

                it("팀 조회 시 등록된 모든 팀의 정보가 조회 되어야 한다.") {
                    // given
                    val team1 = Team(name = "테스트 1팀")
                    val team2 = Team(name = "테스트 2팀")
                    teamService.register(team1)
                    teamService.register(team2)

                    // then
                    val retrieveAll: List<Team> = teamService.retrieveAll()

                    // when
                    retrieveAll.shouldHaveSize(2)
                }

                it("등록 되어 있는 팀이 없는 경우 빈 리스트를 반환해야 한다.") {
                    // then
                    val retrieveAll: List<Team> = teamService.retrieveAll()

                    // when
                    retrieveAll.shouldBeEmpty()
                }
            }
        }
    }
})
