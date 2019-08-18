package fr.inativ.main2


import org.junit.Test
import kotlin.test.assertEquals

class BonjourMondeTest {
    @Test
    fun `Bonjour le monde sdevrait être affiché correctement`() {
        assertEquals("Bonjour monde !", salutations())
    }
}