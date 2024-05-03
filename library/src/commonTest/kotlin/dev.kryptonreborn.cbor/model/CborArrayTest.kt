package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CborArrayTest {
    @Test
    fun testEquals() {
        val cborArray = CborArray()
        assertEquals(cborArray, cborArray)
    }

    @Test
    fun testHashcode() {
        val cborArray1: CborArray = CborArray().apply { add(CborUnicodeString("string")) }
        val cborArray2: CborArray = CborArray().apply { add(CborUnicodeString("string")) }
        assertEquals(cborArray1.hashCode(), cborArray2.hashCode())
    }
}
