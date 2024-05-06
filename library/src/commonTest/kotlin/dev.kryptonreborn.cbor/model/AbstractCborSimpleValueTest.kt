package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborSimpleValueTest(private val value: CborSimpleValue, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, value.encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborSimpleValue)
        assertEquals(value.value, cborElement.value)
        assertEquals(value.simpleValueType, cborElement.simpleValueType)
    }
}

/**
 * false -> 0xf4
 */
class CborSimpleValueTest1 : AbstractCborSimpleValueTest(
    CborSimpleValue(SimpleValueType.FALSE), byteArrayOf(0xf4.toByte())
)

/**
 * true -> 0xf5
 */
class CborSimpleValueTest2 : AbstractCborSimpleValueTest(
    CborSimpleValue(SimpleValueType.TRUE), byteArrayOf(0xf5.toByte())
)

/**
 * nil -> 0xf6
 */
class CborSimpleValueTest3 : AbstractCborSimpleValueTest(
    CborSimpleValue(SimpleValueType.NULL), byteArrayOf(0xf6.toByte())
)

/**
 * undefined -> 0xf7
 */
class CborSimpleValueTest4 : AbstractCborSimpleValueTest(
    CborSimpleValue(SimpleValueType.UNDEFINED), byteArrayOf(0xf7.toByte())
)

/**
 * simple(16) -> 0xf0
 */
class CborSimpleValueTest5 : AbstractCborSimpleValueTest(
    CborSimpleValue(16), byteArrayOf(0xf0.toByte())
)

/**
 * simple(24) -> 0xf818
 */
class CborSimpleValueTest6 : AbstractCborSimpleValueTest(
    CborSimpleValue(24), byteArrayOf(0xf8.toByte(), 0x18)
)

/**
 * simple(255) -> 0xf8ff
 */
class CborSimpleValueTest7 : AbstractCborSimpleValueTest(
    CborSimpleValue(255), byteArrayOf(0xf8.toByte(), 0xff.toByte())
)
