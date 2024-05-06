package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborRationalNumber
import dev.kryptonreborn.cbor.model.CborTag
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CborDecoderTest {
    @Test
    fun shouldSetAutoDecodeInfinitiveMaps() {
        val source: Source = Buffer().apply { write(byteArrayOf(0, 1, 2)) }
        val cborDecoder = CborDecoder(source)
        assertTrue(cborDecoder.autoDecodeInfinitiveMaps)
        cborDecoder.autoDecodeInfinitiveMaps = false
        assertFalse(cborDecoder.autoDecodeInfinitiveMaps)
    }

    @Test
    fun shouldSetAutoDecodeRationalNumbers() {
        val source: Source = Buffer().apply { write(byteArrayOf(0, 1, 2)) }
        val cborDecoder = CborDecoder(source)
        assertTrue(cborDecoder.autoDecodeRationalNumbers)
        cborDecoder.autoDecodeRationalNumbers = false
        assertFalse(cborDecoder.autoDecodeRationalNumbers)
    }

    @Test
    fun shouldSetAutoDecodeLanguageTaggedStrings() {
        val source: Source = Buffer().apply { write(byteArrayOf(0, 1, 2)) }
        val cborDecoder = CborDecoder(source)
        assertTrue(cborDecoder.autoDecodeLanguageTaggedStrings)
        cborDecoder.autoDecodeLanguageTaggedStrings = false
        assertFalse(cborDecoder.autoDecodeLanguageTaggedStrings)
    }

    @Test
    fun shouldThrowOnRationalNumberDecode1() {
        val cborElement: List<CborElement> = CborBuilder().addTag(30).add(true).build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding RationalNumber: not an array", it.message)
        }
    }

    @Test
    fun shouldThrowOnRationalNumberDecode2() {
        val cborElement: List<CborElement> = CborBuilder().addTag(30).addArray().add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding RationalNumber: array size is not 2", it.message)
        }
    }

    @Test
    fun shouldThrowOnRationalNumberDecode3() {
        val cborElement: List<CborElement> = CborBuilder().addTag(30).addArray().add(true).add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding RationalNumber: first data item is not a number", it.message)
        }
    }

    @Test
    fun shouldThrowOnRationalNumberDecode4() {
        val cborElement: List<CborElement> = CborBuilder().addTag(30).addArray().add(1).add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding RationalNumber: second data item is not a number", it.message)
        }
    }

    @Test
    fun shouldDecodeRationalNumber() {
        val cborElement: List<CborElement> = CborBuilder().addTag(30).addArray().add(1).add(2).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertEquals(CborRationalNumber(CborUnsignedInteger(1), CborUnsignedInteger(2)), decoder.decodeNext())
    }

    @Test
    fun shouldDecodeTaggedTags() {
        val decoded: CborElement = CborDecoder.decode(byteArrayOf(0xC1.toByte(), 0xC2.toByte(), 0x02))[0]
        val outer = CborTag(1)
        val inner = CborTag(2)
        val expected = CborUnsignedInteger(2)
        inner.tag = outer
        expected.tag = inner
        assertEquals(expected, decoded)
    }

    @Test
    fun shouldDecodeTaggedRationalNumber() {
        val cborElement: List<CborElement> = CborBuilder().addTag(1).addTag(30).addArray().add(1).add(2).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        val expected = CborRationalNumber(CborUnsignedInteger(1), CborUnsignedInteger(2))
        expected.tag!!.tag = CborTag(1)
        val a = decoder.decodeNext()
        assertEquals(expected, a)
    }

    @Test
    fun shouldThrowOnItemWithForgedLength() {
        val maliciousString = byteArrayOf(0x7a, 0x80.toByte(), 0x00, 0x00, 0x00)
        assertFailsWith<CborException> { CborDecoder.decode(maliciousString) }.also {
            assertEquals("Decoding fixed size items is limited to INTMAX", it.message)
        }
    }
}
