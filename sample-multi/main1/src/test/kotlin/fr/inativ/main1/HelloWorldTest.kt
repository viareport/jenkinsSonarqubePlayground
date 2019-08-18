package fr.inativ.main1

import org.junit.Test

import org.junit.Assert.*

class HelloWorldTest {

    @Test
    fun `Hello World should be corrected displayed`() {
        assertEquals("Hello world !", getGreeting())
    }
}