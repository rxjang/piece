package com.rxjang.piece

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PieceApplication

fun main(args: Array<String>) {
    runApplication<PieceApplication>(*args)
}
