package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborRationalNumber
import dev.kryptonreborn.cbor.model.CborTag
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import dev.kryptonreborn.cbor.model.encodeToBytes
import kotlinx.io.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CborRationalNumberEncoderTest {
    @Test
    fun shouldEncode() {
        val one = CborUnsignedInteger(1)
        val two = CborUnsignedInteger(2)
        val bytes: ByteArray = CborRationalNumber(one, two).encodeToBytes()
        val decoded = CborDecoder(Buffer().apply { write(bytes) }).decodeNext()
        assertTrue(decoded is CborArray)
        assertEquals(CborTag(30), decoded.tag)
        assertEquals(2, decoded.items().size)
        assertEquals(one, decoded.items()[0])
        assertEquals(two, decoded.items()[1])
    }
}
