package org.crispin.hellokopring.employee

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class EmployeeServiceTest : DescribeSpec({

    data class Employee(
        val id: Long?,
        val name: String,
        var teamId: Long? = null,
        var isManager: Boolean = false,
        val enteringDate: LocalDate,
        val birthday: LocalDate,
    )

    class EmployeeFakeRepository {

        private val storage: MutableMap<Long, Employee> = ConcurrentHashMap()
        private var sequence: AtomicLong = AtomicLong(1L)

        fun save(employee: Employee): Employee {
            val id = employee.id ?: sequence.getAndIncrement()
            storage[id] = employee.copy(id = id)
            return storage[id]!!
        }

        fun findAll(): List<Employee> {
            return storage.values.stream().toList() ?: emptyList()
        }
    }

    class EmployeeService(
        val employeeRepository: EmployeeFakeRepository
    ) {
        fun register(employee: Employee): Employee {
            return employeeRepository.save(employee)
        }

        fun retrieveAll(): List<Employee> {
            return employeeRepository.findAll()
        }
    }


    lateinit var employeeService: EmployeeService
    lateinit var employeeRepository: EmployeeFakeRepository

    beforeTest {
        employeeRepository = EmployeeFakeRepository()
        employeeService = EmployeeService(employeeRepository)
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
                    val employeeId: Employee = employeeService.register(employee)

                    // then
                    employeeId.id shouldBe 1L
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
