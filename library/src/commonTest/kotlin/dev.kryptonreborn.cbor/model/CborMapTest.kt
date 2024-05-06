package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CborMapTest {
    @Test
    fun testRemove() {
        val key = CborUnicodeString("key")
        val value = CborUnicodeString("value")
        val cborMap = CborMap()
        cborMap.put(key, value)
        assertEquals(1, cborMap.values().size)
        cborMap.remove(key)
        assertEquals(0, cborMap.values().size)
    }

    @Test
    fun testEquals() {
        val cborMap1 = CborMap()
        assertEquals(cborMap1, cborMap1)
        assertNotEquals(cborMap1, Any())
    }

    @Test
    fun testHashcode() {
        val cborMap1 = CborMap()
        val cborMap2 = CborMap()
        assertEquals(cborMap1.hashCode(), cborMap2.hashCode())
        cborMap1.put(CborUnicodeString("key"), CborUnicodeString("value"))
        assertNotEquals(cborMap1.hashCode(), cborMap2.hashCode())
    }

    @Test
    fun testItemOrderIsPreserved() {
        val input: List<CborElement> = CborBuilder().addMap().put(1, "v1").put(0, "v2").end().build()
        val sourceOut = Buffer()
        val encoder = CborEncoder(sourceOut)
        encoder.apply { canonical = false }.encode(input)
        val sourceIn = Buffer().apply { write(sourceOut.readByteArray()) }
        val decoder = CborDecoder(sourceIn)
        val output: List<CborElement> = decoder.decode()
        assertEquals(input, output)
        val cborElement: CborElement = output[0]
        assertEquals(MajorType.MAP, cborElement.majorType)
        val values: Collection<CborElement> = (cborElement as CborMap).values()
        val iterator: Iterator<CborElement> = values.iterator()
        assertEquals(CborUnicodeString("v1"), iterator.next())
        assertEquals(CborUnicodeString("v2"), iterator.next())
    }
}
