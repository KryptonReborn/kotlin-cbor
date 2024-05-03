package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CborRationalNumberTest {
    @Test
    fun shouldThrowIfDenominatorIsZero() {
        assertFailsWith<IllegalArgumentException> { CborRationalNumber(CborUnsignedInteger(1), CborUnsignedInteger(0)) }
    }

    @Test
    fun shouldSetNumeratorAndDenominator() {
        val one = CborUnsignedInteger(1)
        val two = CborUnsignedInteger(2)
        val cborRationalNumber = CborRationalNumber(one, two)
        assertEquals(one, cborRationalNumber.numerator)
        assertEquals(two, cborRationalNumber.denominator)
    }
}
