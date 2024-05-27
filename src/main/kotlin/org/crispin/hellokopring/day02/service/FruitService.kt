package org.crispin.hellokopring.day02.service

import org.crispin.hellokopring.day02.controller.FruitInfoRequest
import org.crispin.hellokopring.day02.controller.FruitSaleInfoRequest
import org.crispin.hellokopring.day02.controller.SaleResponse
import org.crispin.hellokopring.day02.controller.StateResponse
import org.crispin.hellokopring.day02.domain.Fruit
import org.crispin.hellokopring.day02.domain.State
import org.crispin.hellokopring.day02.repository.FruitMemoryRepository
import org.springframework.stereotype.Service

@Service
class FruitService(
    val fruitMemoryRepository: FruitMemoryRepository
) {
    fun saveInfo(request: FruitInfoRequest) {
        fruitMemoryRepository.save(
            Fruit.of(request.name, request.warehousingDate, request.price)
        )
    }

    fun sell(request: FruitSaleInfoRequest): SaleResponse {
        val findFruit: Fruit? = fruitMemoryRepository.findById(request.id)
        findFruit ?: throw IllegalArgumentException("존재하지 않는 과일 아이디 입니다.")

        val savedFruit: Fruit? = fruitMemoryRepository.save(
            Fruit.of(
                findFruit.id!!,
                findFruit.name,
                findFruit.warehousingDate,
                findFruit.price,
                State.SALE_COMPLETED
            )
        )

        return SaleResponse(savedFruit!!.id!!)
    }

    fun state(name: String): StateResponse {
        val fruitsWithName = fruitMemoryRepository.findAllByName(name)

        val salesAmount = calculateTotalPrice(fruitsWithName, State.SALE_COMPLETED)
        val notSalesAmount = calculateTotalPrice(fruitsWithName, State.SALE)

        return StateResponse(salesAmount, notSalesAmount)
    }

    private fun calculateTotalPrice(fruits: List<Fruit>, state: State): Long {
        return fruits
            .filter { it.state == state }
            .sumOf { it.price }
    }
}
