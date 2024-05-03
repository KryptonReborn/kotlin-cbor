package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MajorTypeTest {
    @Test
    fun shouldParseUnsignedInteger() {
        assertEquals(MajorType.UNSIGNED_INTEGER, MajorType.ofByte(0))
    }

    @Test
    fun shouldParseNegativeInteger() {
        assertEquals(MajorType.NEGATIVE_INTEGER, MajorType.ofByte(32))
    }

    @Test
    fun shouldParseByteString() {
        assertEquals(MajorType.BYTE_STRING, MajorType.ofByte(64))
    }

    @Test
    fun shouldParseUnicodeString() {
        assertEquals(MajorType.UNICODE_STRING, MajorType.ofByte(96))
    }

    @Test
    fun shouldParseArray() {
        assertEquals(MajorType.ARRAY, MajorType.ofByte(128))
    }

    @Test
    fun shouldParseMap() {
        assertEquals(MajorType.MAP, MajorType.ofByte(160))
    }

    @Test
    fun shouldParseTag() {
        assertEquals(MajorType.TAG, MajorType.ofByte(192))
    }

    @Test
    fun shouldParseSpecial() {
        assertEquals(MajorType.SPECIAL, MajorType.ofByte(224))
    }

    @Test
    fun shouldReturnThrowOnInvalidByteValue() {
        assertFailsWith<CborException> { MajorType.ofByte(-0x1) }
    }
}
