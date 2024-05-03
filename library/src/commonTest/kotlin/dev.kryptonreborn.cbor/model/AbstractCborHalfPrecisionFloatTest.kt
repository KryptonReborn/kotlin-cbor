package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborHalfPrecisionFloatTest(private val value: Float, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, CborHalfPrecisionFloat(value).encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborHalfPrecisionFloat)
        assertEquals(value, cborElement.value)
    }
}

/**
 * 0.0 -> 0xf90000
 */
class CborHalfPrecisionFloatTest1 : AbstractCborHalfPrecisionFloatTest(0.0f, byteArrayOf(0xf9.toByte(), 0x00, 0x00))

/**
 * -0.0 -> 0xf98000
 */
class CborHalfPrecisionFloatTest2 :
    AbstractCborHalfPrecisionFloatTest(-0.0f, byteArrayOf(0xf9.toByte(), 0x80.toByte(), 0x00))

/**
 * 1.0 -> 0xf93c00
 */
class CborHalfPrecisionFloatTest3 : AbstractCborHalfPrecisionFloatTest(1.0f, byteArrayOf(0xf9.toByte(), 0x3c, 0x00))

/**
 * 1.5 -> 0xf93e00
 */
class CborHalfPrecisionFloatTest4 : AbstractCborHalfPrecisionFloatTest(1.5f, byteArrayOf(0xf9.toByte(), 0x3e, 0x00))

/**
 * 65504.0 -> 0xf97bff
 */
class CborHalfPrecisionFloatTest5 :
    AbstractCborHalfPrecisionFloatTest(65504.0f, byteArrayOf(0xf9.toByte(), 0x7b, 0xff.toByte()))

/**
 * 5.960464477539063e-08 -> 0xf90001
 */
class CborHalfPrecisionFloatTest6 :
    AbstractCborHalfPrecisionFloatTest("5.960464477539063e-08".toFloat(), byteArrayOf(0xf9.toByte(), 0x00, 0x01))

/**
 * 6.103515625e-05 -> 0xf90400
 */
class CborHalfPrecisionFloatTest7 :
    AbstractCborHalfPrecisionFloatTest("6.103515625e-05".toFloat(), byteArrayOf(0xf9.toByte(), 0x04, 0x00))

/**
 * -4,0 -> 0xf9c400
 */
class CborHalfPrecisionFloatTest8 :
    AbstractCborHalfPrecisionFloatTest("-4.0".toFloat(), byteArrayOf(0xf9.toByte(), 0xc4.toByte(), 0x00))

/**
 * NaN -> 0xf97e00
 */
class CborHalfPrecisionFloatTest9 :
    AbstractCborHalfPrecisionFloatTest(Float.NaN, byteArrayOf(0xf9.toByte(), 0x7e, 0x00))

/**
 * -Infinity -> 0xf9fc00
 */
class CborHalfPrecisionFloatTest10 :
    AbstractCborHalfPrecisionFloatTest(Float.NEGATIVE_INFINITY, byteArrayOf(0xf9.toByte(), 0xfc.toByte(), 0x00))
