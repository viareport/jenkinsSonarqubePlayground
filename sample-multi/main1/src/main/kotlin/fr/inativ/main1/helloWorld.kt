package fr.inativ.main1

fun getGreeting(): String {
    val words = listOf("Hello", "world !")

    return words.joinToString(separator = " ")
}

fun main() {
    println(getGreeting())
}