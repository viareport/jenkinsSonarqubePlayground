package demo

import org.junit.Test
import kotlin.test.assertEquals

class TestSource {
    @Test
    fun `Hello World should be corrected displayed`() {
        assertEquals("Hello world !", getGreeting())
    }
}