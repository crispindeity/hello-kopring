package org.crispin.hellokopring.day02.controller

import org.crispin.hellokopring.day02.service.FruitService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/fruit")
class FruitController(
    val fruitService: FruitService
) {
    @PostMapping("")
    fun info(
        @RequestBody fruitInfoRequest: FruitInfoRequest,
    ) {
        fruitService.saveInfo(fruitInfoRequest)
    }

    @PutMapping("")
    fun sale(
        @RequestBody fruitSaleInfoRequest: FruitSaleInfoRequest,
    ): SaleResponse {
        return fruitService.sell(fruitSaleInfoRequest)
    }

    @GetMapping("/stat")
    fun stat(
        @RequestParam("name") name: String,
    ): StateResponse {
        return fruitService.state(name)
    }
}
