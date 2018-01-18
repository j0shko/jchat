package hr.jtomic.jchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JchatApplication

fun main(args: Array<String>) {
    runApplication<JchatApplication>(*args)
}
