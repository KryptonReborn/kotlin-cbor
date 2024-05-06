package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.MajorType
import dev.kryptonreborn.cbor.model.encodeToBytes
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CborByteStringDecoderTest {
    @Test
    fun shouldDecodeChunkedByteString() {
        val encodedBytes: ByteArray = CborEncoder.encodeToBytes(
            CborBuilder().startByteString()
                .add(byteArrayOf('\u0000'.code.toByte())).add(byteArrayOf(0x10)).add(byteArrayOf(0x13)).end().build()
        )
        val source: Source = Buffer().apply { write(encodedBytes) }
        val decoder = CborDecoder(source)
        val cborElements: List<CborElement> = decoder.decode()
        assertNotNull(cborElements)
        assertEquals(1, cborElements.size.toLong())
    }

    @Test
    fun shouldDecodeByteString1K() {
        val encodedBytes: ByteArray = CborEncoder.encodeToBytes(CborBuilder().add(ByteArray(1024)).build())
        val source: Source = Buffer().apply { write(encodedBytes) }
        val decoder = CborDecoder(source)
        val cborElements: List<CborElement> = decoder.decode()
        assertNotNull(cborElements)
        assertEquals(1, cborElements.size.toLong())
    }

    @Test
    fun shouldDecodeByteString1M() {
        val encodedBytes: ByteArray = CborEncoder.encodeToBytes(CborBuilder().add(ByteArray(1024 * 1024)).build())
        val source: Source = Buffer().apply { write(encodedBytes) }
        val decoder = CborDecoder(source)
        val cborElements: List<CborElement> = decoder.decode()
        assertNotNull(cborElements)
        assertEquals(1, cborElements.size.toLong())
    }

    @Test
    fun shouldThrowOnIncompleteByteString() {
        val bytes = byteArrayOf(0x42, 0x20)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }.also {
            assertEquals("Error reading from input, the source is exhausted", it.message)
        }
    }

    @Test
    fun shouldTrowOnMissingBreak() {
        val bytes = byteArrayOf(0x5f, 0x41, 0x20)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }.also {
            assertEquals("Unexpected end of stream", it.message)
        }
    }

    @Test
    fun decodingExample() {
        val bytes = byteArrayOf(0, 1, 2, 3)
        // Encode
        val encodedBytes: ByteArray = CborByteString(bytes).encodeToBytes()
        // Decode
        val source: Source = Buffer().apply { write(encodedBytes) }
        val decoder = CborDecoder(source)
        val cborElement: CborElement = decoder.decodeNext()!!
        assertEquals(MajorType.BYTE_STRING, cborElement.majorType)
        val byteString: CborByteString = cborElement as CborByteString
        val decodedBytes: ByteArray = byteString.bytes!!
        // Verify
        assertEquals(bytes.size.toLong(), decodedBytes.size.toLong())
        assertEquals(bytes[0].toLong(), decodedBytes[0].toLong())
        assertEquals(bytes[1].toLong(), decodedBytes[1].toLong())
        assertEquals(bytes[2].toLong(), decodedBytes[2].toLong())
        assertEquals(bytes[3].toLong(), decodedBytes[3].toLong())
    }
}
