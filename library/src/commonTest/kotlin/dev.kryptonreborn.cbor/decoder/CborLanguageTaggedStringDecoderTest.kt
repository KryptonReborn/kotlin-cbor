package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborLanguageTaggedString
import dev.kryptonreborn.cbor.model.CborUnicodeString
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CborLanguageTaggedStringDecoderTest {
    @Test
    fun shouldThrowException() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Unexpected end of stream: tag without following data item.", it.message)
        }
    }

    @Test
    fun testExceptionOnNotAnArray() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).add(true).build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding LanguageTaggedString: not an array", it.message)
        }
    }

    @Test
    fun testExceptionOnNot2ElementArray() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).addArray().add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding LanguageTaggedString: array size is not 2", it.message)
        }
    }

    @Test
    fun testExceptionOnNotFirstElementIsString() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).addArray().add(true).add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding LanguageTaggedString: first data item is not an UnicodeString", it.message)
        }
    }

    @Test
    fun testExceptionOnNotSecondElementIsString() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).addArray().add("en").add(true).end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        assertFailsWith<CborException> { decoder.decode() }.also {
            assertEquals("Error decoding LanguageTaggedString: second data item is not an UnicodeString", it.message)
        }
    }

    @Test
    fun testDecoding() {
        val cborElement: List<CborElement> = CborBuilder().addTag(38).addArray().add("en").add("string").end().build()
        val source: Source = Buffer().apply { write(CborEncoder.encodeToBytes(cborElement)) }
        val decoder = CborDecoder(source)
        val item: CborElement = decoder.decodeNext()!!
        assertEquals(CborLanguageTaggedString(CborUnicodeString("en"), CborUnicodeString("string")), item)
    }
}
