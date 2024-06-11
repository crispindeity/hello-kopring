package org.crispin.hellokopring.employee

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository
import org.crispin.hellokopring.mini.employee.service.EmployeeService
import org.crispin.hellokopring.mini.team.domain.Team
import org.crispin.hellokopring.mini.team.repository.TeamRepository
import org.crispin.hellokopring.mock.EmployeeFakeRepository
import org.crispin.hellokopring.mock.TeamFakeRepository
import java.time.LocalDate

class EmployeeServiceTest : DescribeSpec({

    lateinit var employeeService: EmployeeService
    lateinit var employeeRepository: EmployeeRepository
    lateinit var teamRepository: TeamRepository

    beforeTest {
        employeeRepository = EmployeeFakeRepository()
        teamRepository = TeamFakeRepository()
        employeeService = EmployeeService(
            employeeRepository,
            teamRepository
        )
    }

    describe("직원 서비스 테스트") {

        describe("직원 등록 테스트") {

            describe("직원 등록 성공 테스트") {

                it("정상 적인 직원 정보 입력 시 등록에 성공 해야 한다.") {
                    // given
                    val employee = Employee(
                        null,
                        "테스트 직원1",
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )

                    // when
                    val registeredEmployee: Employee = employeeService.register(employee)

                    // then
                    registeredEmployee.id shouldBe 1L
                }

                it("직원 등록 시 매니저 설정이 가능해야 한다.") {
                    // given
                    val team: Team = Team.createTeam(name = "테스트1팀")
                    val employee = Employee(
                        name = "테스트 직원1",
                        teamId = 1L,
                        isManager = true,
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )
                    teamRepository.save(team)

                    // when
                    val registeredEmployee: Employee = employeeService.register(employee)

                    // then
                    registeredEmployee.isManager shouldBe true
                }

                describe("직원 등록 시 매니저로 등록 하면서 기존에 등록되어 있는 매니저 가 있는 경우 서로 변경 되어야 한다.") {
                    // given
                    val team: Team = Team.createTeam(name = "테스트1팀")
                    val oldManagerEmployee = Employee(
                        name = "이전 매니저 직원",
                        teamId = 1L,
                        isManager = true,
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )
                    val newManagerEmployee = oldManagerEmployee.copy(
                        name = "뉴 매니저 직원"
                    )
                    teamRepository.save(team)
                    employeeService.register(oldManagerEmployee)

                    // when
                    val response: Employee = employeeService.register(newManagerEmployee)

                    // then
                    team.managerId shouldBe response.id
                }
            }

            describe("직원 등록 실패 테스트") {

                it("등록되지 않은 팀에 직원을 등록하는 경우 예외가 발생해야 한다.") {
                    // given
                    val employee = Employee(
                        name = "테스트 직원1",
                        teamId = 1L,
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )

                    // when & then
                    shouldThrowExactly<IllegalArgumentException> {
                        employeeService.register(employee)
                    }.message shouldBe "등록되지 않은 팀 아이디 입니다. ${employee.teamId}"
                }

                it("팀에 등록 되어 있지 않은 직원을 매니저로 등록 시 예외가 발생해야 한다.") {
                    // given
                    val employee = Employee(
                        name = "테스트 직원1",
                        isManager = true,
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )

                    // when & then
                    shouldThrowExactly<IllegalArgumentException> {
                        employeeService.register(employee)
                    }.message shouldBe "팀에 등록되지 않은 멤버는 매니저로 설정할 수 없습니다. ${employee.id}"
                }
            }
        }

        describe("직원 직위 수정 테스트") {

            describe("직원 직위 수정 실패 테스트") {

                it("등록되지 않은 직원의 직위를 수정하는 경우 예외가 발생해야 한다.") {
                    // given
                    val employeeId = 1L

                    // when & then
                    shouldThrowExactly<IllegalArgumentException> {
                        employeeService.modifyPosition(employeeId)
                    }.message shouldBe "등록되지 않은 직원 아이디 입니다. $employeeId"
                }

                it("팀에 등록되지 않은 직원의 직위를 수정하는 경우 예외가 발생해야 한다.") {
                    // given
                    val employee = Employee(
                        name = "테스트 팀원1",
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )
                    val registeredEmployee = employeeService.register(employee)

                    // when & then
                    shouldThrowExactly<IllegalArgumentException> {
                        employeeService.modifyPosition(registeredEmployee.id!!)
                    }.message shouldBe "팀에 등록되지 않은 직원 입니다. ${registeredEmployee.id}"
                }
            }
        }

        describe("직원 조회 테스트") {

            describe("직원 조회 성공 테스트") {

                it("직원 조회 시 등록된 모든 직원의 정보가 조회 되어야 한다.") {
                    // given
                    val employee1 = Employee(
                        null,
                        "테스트 직원1",
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(1999, 9, 9),
                    )
                    val employee2 = Employee(
                        null,
                        "테스트 직원2",
                        enteringDate = LocalDate.of(2024, 5, 28),
                        birthday = LocalDate.of(2000, 2, 2),
                    )
                    employeeService.register(employee1)
                    employeeService.register(employee2)

                    // when
                    val retrievedInfo: List<Employee> = employeeService.retrieveAll()

                    // then
                    assertSoftly {
                        retrievedInfo shouldNotBe null
                        retrievedInfo.shouldHaveSize(2)
                    }
                }

                it("등록된 직원이 없는 경우 빈 리스트가 반환 되어야 한다.") {
                    // when
                    val retrievedInfo: List<Employee> = employeeService.retrieveAll()

                    // then
                    retrievedInfo.shouldBeEmpty()
                }
            }
        }
    }
})
