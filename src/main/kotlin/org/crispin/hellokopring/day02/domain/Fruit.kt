package org.crispin.hellokopring.day02.domain

import java.time.LocalDate

data class Fruit(
    var id: Long?,
    val name: String,
    val warehousingDate: LocalDate,
    val price: Long,
    var state: State = State.SALE,
) {
    companion object Factory {
        fun of(name: String, warehousingDate: LocalDate, price: Long): Fruit {
            return Fruit(null, name, warehousingDate, price)
        }
        fun of(id: Long, name: String, warehousingDate: LocalDate, price: Long, state: State): Fruit {
            return Fruit(id, name, warehousingDate, price, state)
        }
    }
}
