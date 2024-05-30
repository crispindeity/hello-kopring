package org.crispin.hellokopring.team

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository
import org.crispin.hellokopring.mini.team.domain.Team
import org.crispin.hellokopring.mini.team.repository.TeamRepository
import org.crispin.hellokopring.mini.team.service.TeamService
import org.crispin.hellokopring.mock.EmployeeFakeRepository
import org.crispin.hellokopring.mock.TeamFakeRepository
import java.time.LocalDate

class TeamServiceTest : DescribeSpec({

    lateinit var teamService: TeamService
    lateinit var teamRepository: TeamRepository
    lateinit var employeeRepository: EmployeeRepository

    beforeTest {
        teamRepository = TeamFakeRepository()
        employeeRepository = EmployeeFakeRepository()
        teamService = TeamService(
            teamRepository, employeeRepository
        )
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

            describe("팀 등록 실패 테스트") {

                it("등록되지 않은 직원을 팀 매니저로 지정하는 경우 예외가 발생해야 한다.") {
                    // given
                    val team = Team(
                        name = "테스트1팀",
                        managerId = 1L
                    )

                    // when & then
                    shouldThrowExactly<IllegalArgumentException> {
                        teamService.register(team)
                    }.message shouldBe "등록되지 않은 직원 아이디 입니다. ${team.managerId}"
                }

                it("해당 팀에 등록되지 않은 직원을 팀 매니저로 지정하는 겨우 예외가 발생해야 한다.") {
                    // given
                    val team = Team(
                        name = "테스트1팀",
                        managerId = 1L,
                    )
                    val employee = Employee(
                        name = "테스트 직원1",
                        teamId = 2L,
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )
                    employeeRepository.save(employee)

                    // when & then
                    shouldThrowAny {
                        teamService.register(team)
                    }
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
