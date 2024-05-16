package dev.kryptonreborn.cbor.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborNumberTest {
    private val value: BigInteger
    private val encodedValue: ByteArray

    constructor(value: Long, encodedValue: ByteArray) {
        this.value = BigInteger.fromLong(value)
        this.encodedValue = encodedValue
    }

    constructor(value: BigInteger, encodedValue: ByteArray) {
        this.value = value
        this.encodedValue = encodedValue
    }

    @Test
    fun shouldEncode() {
        val cborElements: List<CborElement> = CborBuilder().add(value).build()
        assertContentEquals(encodedValue, cborElements[0].encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborNumber)
        assertEquals(value, cborElement.value)
    }
}

/**
 * 0 -> 0x00
 */
class CborNumberTest1 : AbstractCborNumberTest(0, byteArrayOf(0x00))

/**
 * 0 -> 0x00
 */
class CborNumberTest2 : AbstractCborNumberTest(1, byteArrayOf(0x01))

/**
 * 10 -> 0x0a
 */
class CborNumberTest3 : AbstractCborNumberTest(10, byteArrayOf(0x0a))

/**
 * 23 -> 0x17
 */
class CborNumberTest4 : AbstractCborNumberTest(23, byteArrayOf(0x17))

/**
 * 24 -> 0x1818
 */
class CborNumberTest5 : AbstractCborNumberTest(24, byteArrayOf(0x18, 0x18))

/**
 * 25 -> 0x1819
 */
class CborNumberTest6 : AbstractCborNumberTest(25, byteArrayOf(0x18, 0x19))

/**
 * 100 -> 0x1864
 */
class CborNumberTest7 : AbstractCborNumberTest(100, byteArrayOf(0x18, 0x64))

/**
 * 1000 -> 0x1903e8
 */
class CborNumberTest8 : AbstractCborNumberTest(1000, byteArrayOf(0x19, 0x03, 0xe8.toByte()))

/**
 * 1000000 -> 0x1a000f4240
 */
class CborNumberTest9 : AbstractCborNumberTest(1000000, byteArrayOf(0x1a, 0x00, 0x0f, 0x42, 0x40))

/**
 * 1000000000000 -> 0x1b 00 00 00 e8 d4 a5 10 00
 */
class CborNumberTest10 : AbstractCborNumberTest(
    1000000000000L,
    byteArrayOf(0x1b, 0x00, 0x00, 0x00, 0xe8.toByte(), 0xd4.toByte(), 0xa5.toByte(), 0x10, 0x00),
)

/**
 * 18446744073709551615 -> 0x1bffffffffffffffff
 */
class CborNumberTest11 : AbstractCborNumberTest(
    BigInteger.parseString("18446744073709551615"),
    byteArrayOf(
        0x1b, 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
        0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
    ),
)

/**
 * -18446744073709551616 -> 0x3bffffffffffffffff
 */
class CborNumberTest12 : AbstractCborNumberTest(
    BigInteger.parseString("-18446744073709551616"),
    byteArrayOf(
        0x3b.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
        0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
    ),
)

/**
 * -1 -> 0x20
 */
class CborNumberTest13 : AbstractCborNumberTest(-1, byteArrayOf(0x20))

/**
 * -10 -> 0x29
 */
class CborNumberTest14 : AbstractCborNumberTest(-10, byteArrayOf(0x29))

/**
 * -100 -> 0x3863
 */
class CborNumberTest15 : AbstractCborNumberTest(-100, byteArrayOf(0x38, 0x63))

/**
 * -1000 -> 0x3903e7
 */
class CborNumberTest16 : AbstractCborNumberTest(-1000, byteArrayOf(0x39, 0x03, 0xe7.toByte()))
