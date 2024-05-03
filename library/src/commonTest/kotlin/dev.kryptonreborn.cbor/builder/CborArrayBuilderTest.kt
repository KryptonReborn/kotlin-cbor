package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.CborDoublePrecisionFloat
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborFalse
import dev.kryptonreborn.cbor.model.CborHalfPrecisionFloat
import dev.kryptonreborn.cbor.model.CborTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CborArrayBuilderTest {
    @Test
    fun shouldAddBoolean() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> = builder.addArray().add(true).add(false).end().build()
        assertEquals(1, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborArray)
        val cborArray = cborElements[0] as CborArray
        assertEquals(2, cborArray.items().size)
        assertTrue(cborArray.items()[0] is CborTrue)
        assertTrue(cborArray.items()[1] is CborFalse)
    }

    @Test
    fun shouldAddFloat() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> = builder.addArray().add(1.0f).end().build()
        assertEquals(1, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborArray)
        val cborArray = cborElements[0] as CborArray
        assertEquals(1, cborArray.items().size)
        assertTrue(cborArray.items()[0] is CborHalfPrecisionFloat)
    }

    @Test
    fun shouldAddDouble() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> = builder.addArray().add(1.0).end().build()
        assertEquals(1, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborArray)
        val cborArray = cborElements[0] as CborArray
        assertEquals(1, cborArray.items().size)
        assertTrue(cborArray.items()[0] is CborDoublePrecisionFloat)
    }

    @Test
    fun shouldAddByteArray() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> = builder.addArray().add(byteArrayOf(0x0)).end().build()
        assertEquals(1, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborArray)
        val cborArray = cborElements[0] as CborArray
        assertEquals(1, cborArray.items().size)
        assertTrue(cborArray.items()[0] is CborByteString)
    }
}
