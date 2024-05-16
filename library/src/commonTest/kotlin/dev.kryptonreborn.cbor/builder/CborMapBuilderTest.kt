package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.model.CborUnicodeString
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CborMapBuilderTest {
    @Test
    fun testMapBuilder() {
        val cborElements: List<CborElement> =
            CborBuilder()
                .addMap()
                .put(CborUnicodeString("key"), CborUnicodeString("value"))
                .put(1, true)
                .put(2, "value".encodeToByteArray())
                .put(3, 1.0)
                .put(4, 1.0f)
                .put(5, 1L)
                .put(6, "value")
                .put("7", true)
                .put("8", "value".encodeToByteArray())
                .put("9", 1.0)
                .put("10", 1.0f)
                .put("11", 1L)
                .put("12", "value")
                .putMap(13)
                .end()
                .putMap("14").end()
                .putMap(CborUnsignedInteger(15)).end()
                .putArray(16).end()
                .putArray("17").end()
                .putArray(CborUnsignedInteger(18)).end()
                .addKey(19).value(true)
                .addKey(20).value("value".encodeToByteArray())
                .addKey(21).value(1.0)
                .addKey(22).value(1.0)
                .addKey(23).value(1.0)
                .addKey(24).value("value")
                .addKey("25").value(true)
                .addKey("26").value("value".encodeToByteArray())
                .addKey("27").value(1.0)
                .addKey("28").value(1.0)
                .addKey("29").value(1.0)
                .addKey("30").value("value")
                .end()
                .startMap()
                .startArray(1).end()
                .startArray(CborUnsignedInteger(2)).end()
                .end()
                .build()
        assertEquals(2, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborMap)
        assertEquals(31, (cborElements[0] as CborMap).keys().size)
    }

    @Test
    fun startMapInMap() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> =
            builder.addMap().startMap(CborByteString(byteArrayOf(0x01))).put(1, 2).end()
                .startMap(1).end().startMap("asdf").end().end().build()
        val rootMap = cborElements[0] as CborMap
        val keys = rootMap.keys().toList()
        assertEquals(keys[0], CborByteString(byteArrayOf(0x01)))
        assertEquals(keys[1], CborUnsignedInteger(1))
        assertEquals(keys[2], CborUnicodeString("asdf"))
        assertTrue(rootMap.get(keys[0]) is CborMap)
        assertTrue(rootMap.get(keys[1]) is CborMap)
        assertTrue(rootMap.get(keys[2]) is CborMap)
    }
}
