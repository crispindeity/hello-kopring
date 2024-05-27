package org.crispin.hellokopring.day01.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CalcController {

    @GetMapping("/calc")
    fun calc(
        @ModelAttribute numbers: Numbers
    ): CalcResultResponse {
        return CalcResultResponse(
            numbers.number1 + numbers.number2,
            numbers.number1 - numbers.number2,
            numbers.number1 * numbers.number2
        )
    }

    @PostMapping("/calc")
    fun calc(
        @RequestBody numberList: NumberList
    ): Int {
        return numberList.numbers.sum()
    }
}
