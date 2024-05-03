package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class CborSimpleValueTest {
    @Test
    fun testEquals1() {
        for (i in 0..255) {
            val cborSimpleValue1 = CborSimpleValue(i)
            val cborSimpleValue2 = CborSimpleValue(i)
            assertEquals(cborSimpleValue1, cborSimpleValue2)
        }
    }

    @Test
    fun testEquals2() {
        val cborSimpleValue = CborSimpleValue(0)
        assertNotEquals(cborSimpleValue, CborSimpleValue(1))
        assertFalse(cborSimpleValue.equals(null))
        assertFalse(cborSimpleValue.equals(false))
        assertFalse(cborSimpleValue.equals(""))
        assertFalse(cborSimpleValue.equals(1))
        assertFalse(cborSimpleValue.equals(1.1))
    }
}
