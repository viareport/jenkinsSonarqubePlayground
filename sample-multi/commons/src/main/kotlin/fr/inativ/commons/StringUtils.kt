package fr.inativ.commons

fun convertToUpperCase(s: String?): String {
    if (s == null) return ""
    else return s.toUpperCase()
}


private fun String.enMajuscule() = convertToUpperCase(this)

fun maFonctionAMoi(s1: String, s2: String): String {
    println("Analyse et conversion de $s1 et $s2")
    val S1 = s1.enMajuscule()
    val S2 = s2.enMajuscule()
    println("Passage en majuscule pour obtenir $S1 et $S2")

    val neSertARien = emptyList<Int>()
    (0..10).fold(neSertARien, { acc, i ->
        acc + listOf(i)
    })

    return S2 + " | " + S1
}
