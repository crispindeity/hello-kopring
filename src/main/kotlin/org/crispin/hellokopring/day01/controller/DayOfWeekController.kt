package org.crispin.hellokopring.day01.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalDate

@RestController
@RequestMapping("/api/v1")
class DayOfWeekController {

    @GetMapping("/day-of-the-week")
    fun dayOfWeek(
        @RequestParam("date") date: String,
    ): DayOfWeekResultResponse {
        val dayOfWeek: DayOfWeek = LocalDate.parse(date).dayOfWeek
        return DayOfWeekResultResponse(dayOfWeek)
    }
}
