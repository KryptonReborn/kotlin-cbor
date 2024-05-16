package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborDoublePrecisionFloatTest(private val value: Double, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        val cborElements: List<CborElement> = CborBuilder().add(value).build()
        assertContentEquals(encodedValue, cborElements[0].encodeToBytes())
    }

    @Test
    @Throws(CborException::class)
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborDoublePrecisionFloat)
        assertEquals(value, cborElement.value, 0.0)
    }
}

/**
 * 1.1 -> 0xfb 3f f1 99 99 99 99 99 9a
 */
class CborDoublePrecisionFloatTest1 : AbstractCborDoublePrecisionFloatTest(
    1.1,
    byteArrayOf(
        0xfb.toByte(), 0x3f, 0xf1.toByte(), 0x99.toByte(), 0x99.toByte(), 0x99.toByte(), 0x99.toByte(),
        0x99.toByte(), 0x9a.toByte(),
    ),
)

/**
 * 1.0e+300 -> 0xfb 7e 37 e4 3c 88 00 75 9c
 */
class CborDoublePrecisionFloatTest2 : AbstractCborDoublePrecisionFloatTest(
    "1.0e+300".toDouble(),
    byteArrayOf(0xfb.toByte(), 0x7e, 0x37, 0xe4.toByte(), 0x3c, 0x88.toByte(), 0x00, 0x75, 0x9c.toByte()),
)

/**
 * -4.1 -> 0xfb c0 10 66 66 66 66 66 66
 */
class CborDoublePrecisionFloatTest3 : AbstractCborDoublePrecisionFloatTest(
    "-4.1".toDouble(),
    byteArrayOf(0xfb.toByte(), 0xc0.toByte(), 0x10, 0x66, 0x66, 0x66, 0x66, 0x66, 0x66),
)

/**
 * Infinity -> 0xfb 7f f0 00 00 00 00 00 00
 */
class CborDoublePrecisionFloatTest4 : AbstractCborDoublePrecisionFloatTest(
    Double.POSITIVE_INFINITY,
    byteArrayOf(0xfb.toByte(), 0x7f, 0xf0.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
)

/**
 * NaN -> 0xfb 7f f8 00 00 00 00 00 00
 */
class CborDoublePrecisionFloatTest5 : AbstractCborDoublePrecisionFloatTest(
    Double.NaN,
    byteArrayOf(0xfb.toByte(), 0x7f, 0xf8.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
)

/**
 * -Infinity -> 0xfb ff f0 00 00 00 00 00 00
 */
class CborDoublePrecisionFloatTest6 : AbstractCborDoublePrecisionFloatTest(
    Double.NEGATIVE_INFINITY,
    byteArrayOf(0xfb.toByte(), 0xff.toByte(), 0xf0.toByte(), 0x00, 0x00, 0x00, 0x00, 0x00, 0x00),
)
