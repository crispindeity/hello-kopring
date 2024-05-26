package org.crispin.hellokopring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HelloKopringApplication

fun main(args: Array<String>) {
    runApplication<HelloKopringApplication>(*args)
}
