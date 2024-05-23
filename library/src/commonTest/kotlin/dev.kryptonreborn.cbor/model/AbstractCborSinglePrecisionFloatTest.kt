package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborSinglePrecisionFloatTest(private val value: Float, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, CborSinglePrecisionFloat(value).encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborSinglePrecisionFloat)
        assertEquals(value, cborElement.value)
    }
}

/**
 * 100000.0 -> 0xfa47c35000
 */
class CborSinglePrecisionFloatTest1 :
    AbstractCborSinglePrecisionFloatTest(100000.0f, byteArrayOf(0xfa.toByte(), 0x47, 0xc3.toByte(), 0x50, 0x00))

/**
 * 3.4028234663852886e+38 -> 0xfa 7f 7f ff ff
 */
class CborSinglePrecisionFloatTest2 : AbstractCborSinglePrecisionFloatTest(
    "3.4028234663852886e+38".toFloat(),
    byteArrayOf(0xfa.toByte(), 0x7f, 0x7f.toByte(), 0xff.toByte(), 0xff.toByte()),
)

/**
 * Infinity -> 0xfa 7f 80 00 00
 */
class CborSinglePrecisionFloatTest3 : AbstractCborSinglePrecisionFloatTest(
    Float.POSITIVE_INFINITY,
    byteArrayOf(0xfa.toByte(), 0x7f, 0x80.toByte(), 0x00, 0x00),
)

/**
 * NaN -> 0xfa7fc00000
 */
class CborSinglePrecisionFloatTest4 :
    AbstractCborSinglePrecisionFloatTest(Float.NaN, byteArrayOf(0xfa.toByte(), 0x7f, 0xc0.toByte(), 0x00, 0x00))

/**
 * -Infinity -> 0xfa ff 80 00 00
 */
class CborSinglePrecisionFloatTest5 : AbstractCborSinglePrecisionFloatTest(
    Float.NEGATIVE_INFINITY,
    byteArrayOf(0xfa.toByte(), 0xff.toByte(), 0x80.toByte(), 0x00, 0x00),
)
