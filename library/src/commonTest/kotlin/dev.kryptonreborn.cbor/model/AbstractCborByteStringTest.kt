package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertTrue

abstract class AbstractCborByteStringTest(private val value: ByteArray, private val encodedValue: ByteArray) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, CborByteString(value).encodeToBytes())
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertTrue(cborElement is CborByteString)
        assertContentEquals(value, cborElement.bytes)
    }
}

/**
 * h'' -> 0x40
 */
class CborByteStringTest1 : AbstractCborByteStringTest(byteArrayOf(), byteArrayOf(0x40))

/**
 * h'01020304' -> 0x4401020304
 */
class CborByteStringTest2 :
    AbstractCborByteStringTest(byteArrayOf(0x01, 0x02, 0x03, 0x04), byteArrayOf(0x44, 0x01, 0x02, 0x03, 0x04))
