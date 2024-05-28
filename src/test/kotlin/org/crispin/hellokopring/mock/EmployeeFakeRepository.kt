package org.crispin.hellokopring.mock

import org.crispin.hellokopring.mini.employee.domain.Employee
import org.crispin.hellokopring.mini.employee.repository.EmployeeRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class EmployeeFakeRepository : EmployeeRepository {

    private val storage: MutableMap<Long, Employee> = ConcurrentHashMap()
    private var sequence: AtomicLong = AtomicLong(1L)

    override fun save(employee: Employee): Employee {
        val id = employee.id ?: sequence.getAndIncrement()
        storage[id] = employee.copy(id = id)
        return storage[id]!!
    }

    override fun findAll(): List<Employee> {
        return storage.values.stream().toList() ?: emptyList()
    }
}
