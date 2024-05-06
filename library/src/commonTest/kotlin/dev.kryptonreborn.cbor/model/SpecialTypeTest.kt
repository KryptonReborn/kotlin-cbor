package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SpecialTypeTest {
    @Test
    fun shouldDetectUnallocated28() {
        assertFailsWith<CborException> { SpecialType.ofByte(28) }.also {
            assertEquals("Not implemented special type 28", it.message)
        }
    }

    @Test
    fun shouldDetectUnallocated29() {
        assertFailsWith<CborException> { SpecialType.ofByte(29) }.also {
            assertEquals("Not implemented special type 29", it.message)
        }
    }

    @Test
    fun shouldDetectUnallocated30() {
        assertFailsWith<CborException> { SpecialType.ofByte(30) }.also {
            assertEquals("Not implemented special type 30", it.message)
        }
    }
}
