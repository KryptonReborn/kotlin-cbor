package dev.kryptonreborn.cbor.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

class CborUnsignedIntegerTest : AbstractCborElementTest() {
    @Test
    fun shouldEncodeAndDecode() {
        val maxInteger = 4294967295L
        var i = 0L
        while (i < maxInteger) {
            shouldEncodeAndDecode(i.toString(), CborUnsignedInteger(i))
            i += (maxInteger / 100000L)
        }
        shouldEncodeAndDecode(maxInteger.toString(), CborUnsignedInteger(maxInteger))
    }

    @Test
    fun shouldNotAcceptNegativeValues() {
        assertFailsWith<IllegalArgumentException> { CborUnsignedInteger(-1) }
    }

    @Test
    fun testEquals1() {
        val cborUnsignedInteger = CborUnsignedInteger(BigInteger.ZERO)
        assertEquals(cborUnsignedInteger, cborUnsignedInteger)
    }

    @Test
    fun testEquals2() {
        for (i in 0..255) {
            val cborUnsignedInteger1 = CborUnsignedInteger(BigInteger.fromLong(i.toLong()))
            val cborUnsignedInteger2 = CborUnsignedInteger(BigInteger(i.toLong()))
            assertEquals(cborUnsignedInteger1, cborUnsignedInteger2)
        }
    }

    @Test
    fun testEquals3() {
        val cborUnsignedInteger = CborUnsignedInteger(BigInteger.ZERO)
        assertNotEquals(cborUnsignedInteger, Any())
    }

    @Test
    fun testHashcode() {
        val cborUnsignedInteger = CborUnsignedInteger(BigInteger.ZERO)
        assertEquals(BigInteger.ZERO.hashCode(), cborUnsignedInteger.value.hashCode())
    }
}
