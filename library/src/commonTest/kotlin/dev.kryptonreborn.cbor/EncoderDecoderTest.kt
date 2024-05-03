package dev.kryptonreborn.cbor

import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborNegativeInteger
import dev.kryptonreborn.cbor.model.CborTag
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EncoderDecoderTest {
    @Test
    fun test() {
        val a = CborUnsignedInteger(1)
        val x = CborNegativeInteger(-2)
        val source = Buffer()
        val encoder = CborEncoder(source)
        encoder.encode(a)
        encoder.encode(x)
        val bytes: ByteArray = source.readByteArray()
        val decoded: CborElement = CborDecoder.decode(bytes)[0]
        assertEquals(a, decoded)
    }

    @Test
    fun testTagging() {
        val a = CborUnsignedInteger(1)
        val x = CborNegativeInteger(-2)
        a.tag = CborTag(1)
        val source = Buffer()
        val encoder = CborEncoder(source)
        encoder.encode(a)
        encoder.encode(x)
        val bytes: ByteArray = source.readByteArray()
        val decoded: CborElement = CborDecoder.decode(bytes)[0]
        assertEquals(a, decoded)
        assertTrue(decoded.hasTag())
        assertEquals(1L, decoded.tag!!.value)
    }
}
