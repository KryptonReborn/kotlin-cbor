package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CborDoublePrecisionFloatTest {
    @Test
    fun testEquals() {
        val a = CborDoublePrecisionFloat(1.234)
        val b = CborDoublePrecisionFloat(0.333)
        assertEquals(a, a)
        assertEquals(b, b)
    }

    @Test
    fun testNotEquals() {
        val cborDoublePrecisionFloat = CborDoublePrecisionFloat(1.234)
        assertNotEquals(cborDoublePrecisionFloat, CborDoublePrecisionFloat(1.2345))
    }
}
