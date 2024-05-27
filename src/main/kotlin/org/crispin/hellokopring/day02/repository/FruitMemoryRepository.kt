package org.crispin.hellokopring.day02.repository

import org.crispin.hellokopring.day02.domain.Fruit
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

@Repository
class FruitMemoryRepository {

    private val storage = HashMap<Long, Fruit>()
    private var sequence = AtomicLong(1)

    fun save(fruit: Fruit): Fruit? {
        return if (fruit.id == null || storage[fruit.id] == null) {
            val sequence = sequence.getAndIncrement()
            storage[sequence] = Fruit.of(
                sequence,
                fruit.name,
                fruit.warehousingDate,
                fruit.price,
                fruit.state,
            )
            storage[sequence]
        } else {
            storage[fruit.id!!] = fruit
            storage[fruit.id]
        }
    }

    fun findById(id: Long): Fruit? {
        return storage.values.let {
            fruits -> fruits.find { it.id == id }
        }
    }

    fun findAllByName(name: String): List<Fruit> {
        return storage.values.let {
            fruits -> fruits.filter { it.name == name }
        }
    }
}
