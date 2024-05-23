package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractCborUnicodeStringTest(private val value: String, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, CborUnicodeString(value).encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborUnicodeString)
        assertEquals(value, cborElement.toString())
    }
}

/**
 * "" -> 0x60
 */
class CborUnicodeStringTest1 : AbstractCborUnicodeStringTest("", byteArrayOf(0x60))

/**
 * "a" -> 0x6161
 */
class CborUnicodeStringTest2 : AbstractCborUnicodeStringTest("a", byteArrayOf(0x61, 0x61))

/**
 * "IETF" -> 0x64 49 45 54 46
 */
class CborUnicodeStringTest3 : AbstractCborUnicodeStringTest("IETF", byteArrayOf(0x64, 0x49, 0x45, 0x54, 0x46))

/**
 * "\"\\" -> 0x62225c
 */
class CborUnicodeStringTest4 : AbstractCborUnicodeStringTest("\"\\", byteArrayOf(0x62, 0x22, 0x5c))

/**
 * "\u00fc" -> 0x62c3bc
 */
class CborUnicodeStringTest5 : AbstractCborUnicodeStringTest("\u00fc", byteArrayOf(0x62, 0xc3.toByte(), 0xbc.toByte()))

/**
 * "\u6c34" -> 0x63e6b0b4
 */
class CborUnicodeStringTest6 :
    AbstractCborUnicodeStringTest("\u6c34", byteArrayOf(0x63, 0xe6.toByte(), 0xb0.toByte(), 0xb4.toByte()))

/**
 * "\ud800\udd51" -> 0x64f0908591
 */
class CborUnicodeStringTest7 :
    AbstractCborUnicodeStringTest(
        "\ud800\udd51",
        byteArrayOf(0x64, 0xf0.toByte(), 0x90.toByte(), 0x85.toByte(), 0x91.toByte()),
    )
