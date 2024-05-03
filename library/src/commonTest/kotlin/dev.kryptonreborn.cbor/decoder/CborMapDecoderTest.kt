package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CborMapDecoderTest {
    @Test
    fun shouldThrowOnMissingKeyInCborMap() {
        val bytes = byteArrayOf(0xa2.toByte(), 0x01, 0x02)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }
    }

    @Test
    fun shouldThrowOnMissingValueInCborMap() {
        val bytes = byteArrayOf(0xa2.toByte(), 0x01, 0x02, 0x03)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }
    }

    @Test
    fun shouldThrowOnIncompleteIndefiniteLengthCborMap() {
        val bytes = byteArrayOf(0xbf.toByte(), 0x61, 0x01)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }
    }

    @Test
    fun shouldUseLastOfDuplicateKeysByDefault() {
        val bytes = byteArrayOf(0xa2.toByte(), 0x01, 0x01, 0x01, 0x02)
        val decoded: List<CborElement> = CborDecoder.decode(bytes)
        val cborMap = decoded[0] as CborMap
        assertEquals(cborMap.keys().size, 1)
        assertEquals(cborMap.get(CborUnsignedInteger(1)), CborUnsignedInteger(2))
    }

    @Test
    fun shouldThrowOnDuplicateKeyIfEnabled() {
        val bytes = byteArrayOf(0xa2.toByte(), 0x01, 0x01, 0x01, 0x02)
        val source: Source = Buffer().apply { write(bytes) }
        val decoder = CborDecoder(source)
        decoder.rejectDuplicateKeys = true
        assertFailsWith<CborException> { decoder.decode() }
    }

    @Test
    fun shouldThrowInDuplicateKeyInIndefiniteLengthCborMapIfEnabled() {
        val bytes = byteArrayOf(0xbf.toByte(), 0x01, 0x01, 0x01, 0x02, 0xff.toByte())
        val source: Source = Buffer().apply { write(bytes) }
        val decoder = CborDecoder(source)
        decoder.rejectDuplicateKeys = true
        assertFailsWith<CborException> { decoder.decode() }
    }
}
