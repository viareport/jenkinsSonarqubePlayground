package fr.inativ.main2

fun salutations(): String {
    val words = listOf("Bonjour", "monde !")

    return words.joinToString(separator = " ")
}

fun main() {
    println(salutations())
}