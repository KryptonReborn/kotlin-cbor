package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertFailsWith

class CborNegativeIntegerTest : AbstractCborElementTest() {
    @Test
    fun shouldEncodeAndDecode() {
        val maxInteger = -4294967295L
        var i = -1L
        while (i >= maxInteger) {
            shouldEncodeAndDecode(i.toString(), CborNegativeInteger(i))
            i += maxInteger / 100000L
        }
        shouldEncodeAndDecode(maxInteger.toString(), CborNegativeInteger(maxInteger))

        // Test for issue #1: Creation of 64-bit NegativeInteger >= -4294967296L fails
        shouldEncodeAndDecode("Long.MIN_VALUE", CborNegativeInteger(Long.MIN_VALUE))
    }

    @Test
    fun shouldNotAcceptPositiveValues() {
        assertFailsWith<IllegalArgumentException> { CborNegativeInteger(0) }
    }
}
