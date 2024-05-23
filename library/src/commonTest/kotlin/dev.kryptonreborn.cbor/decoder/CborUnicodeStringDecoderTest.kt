package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborElement
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CborUnicodeStringDecoderTest {
    @Test
    fun shouldDecodeChunkedUnicodeString() {
        val encodedBytes: ByteArray =
            CborEncoder.encodeToBytes(
                CborBuilder().startString().add("foo").add("bar").end().build(),
            )
        val source: Source = Buffer().apply { write(encodedBytes) }
        val decoder = CborDecoder(source)
        val cborElements: List<CborElement> = decoder.decode()
        assertNotNull(cborElements)
        assertEquals(1, cborElements.size.toLong())
    }

    @Test
    fun shouldThrowOnIncompleteString() {
        val bytes = byteArrayOf(0x62, 0x61)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }.also {
            assertEquals("Error reading from input, the source is exhausted", it.message)
        }
    }

    @Test
    fun shouldThrowOnMissingBreak() {
        val bytes = byteArrayOf(0x7f, 0x61, 0x61)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }.also {
            assertEquals("Unexpected end of stream", it.message)
        }
    }
}
