package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class CborUnicodeStringTest {
    @Test
    fun toStringShouldPrintNull() {
        assertEquals("null", CborUnicodeString(null).toString())
    }

    @Test
    fun shouldEqualsSameObject() {
        val cborUnicodeString = CborUnicodeString("string")
        assertEquals(cborUnicodeString, cborUnicodeString)
    }

    @Test
    fun shouldEqualsBothNull() {
        val cborUnicodeString1 = CborUnicodeString(null)
        val cborUnicodeString2 = CborUnicodeString(null)
        assertEquals(cborUnicodeString1, cborUnicodeString2)
        assertEquals(cborUnicodeString2, cborUnicodeString1)
    }

    @Test
    fun shouldEqualsSameValue() {
        val cborUnicodeString1 = CborUnicodeString("string")
        val cborUnicodeString2 = CborUnicodeString("string")
        assertEquals(cborUnicodeString1, cborUnicodeString2)
        assertEquals(cborUnicodeString2, cborUnicodeString1)
    }

    @Test
    fun shouldHashNull() {
        assertEquals(0, CborUnicodeString(null).hashCode())
    }

    @Test
    fun shouldNotEqualOtherObjects() {
        val cborUnicodeString = CborUnicodeString("string")
        assertFalse(cborUnicodeString.equals(null))
        assertFalse(cborUnicodeString.equals(1))
        assertFalse(cborUnicodeString.equals("string"))
    }

    @Test
    fun shouldNotEquals() {
        val cborUnicodeString1 = CborUnicodeString(null)
        val cborUnicodeString2 = CborUnicodeString("")
        assertNotEquals(cborUnicodeString1, cborUnicodeString2)
    }
}
