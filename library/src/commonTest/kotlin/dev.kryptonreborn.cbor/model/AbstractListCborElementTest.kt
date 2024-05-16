package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertContentEquals

abstract class AbstractListCborElementTest(
    private val value: List<CborElement>,
    private val encodedValue: ByteArray,
    private val autoDecodeInfinitive: Boolean = true,
) {
    @Test
    fun shouldEncode() {
        assertContentEquals(encodedValue, CborEncoder.encodeToBytes(value))
    }

    @Test
    fun shouldDecode() {
        val source: Source = Buffer().apply { write(encodedValue) }
        val decoder = CborDecoder(source)
        decoder.apply {
            autoDecodeInfinitiveArrays = autoDecodeInfinitive
            autoDecodeInfinitiveUnicodeStrings = autoDecodeInfinitive
            autoDecodeInfinitiveByteStrings = autoDecodeInfinitive
        }
        val cborElement: List<CborElement> = decoder.decode()
        assertContentEquals(value, cborElement)
    }
}

class ListCborElementTest1 : AbstractListCborElementTest(
    CborBuilder().addArray()
        .add("foo").tagged(300).tagged(301).tagged(302)
        .add("bar").tagged(400)
        .addMap()
        .put("a", "b")
        .addKey("c").tagged(401).value("d").tagged(402)
        .end().tagged(403)
        .addArray()
        .add("c").tagged(503)
        .end().tagged(504)
        .end()
        .build(),
    byteArrayOf(
        0x84.toByte(), 0xD9.toByte(), 0x01, 0x2E, 0xD9.toByte(), 0x01, 0x2D, 0xD9.toByte(),
        0x01, 0x2C, 0x63, 0x66, 0x6F, 0x6F, 0xD9.toByte(), 0x01, 0x90.toByte(), 0x63, 0x62,
        0x61, 0x72, 0xD9.toByte(), 0x01, 0x93.toByte(), 0xA2.toByte(), 0x61, 0x61, 0x61, 0x62,
        0xD9.toByte(), 0x01, 0x91.toByte(), 0x61, 0x63, 0xD9.toByte(), 0x01, 0x92.toByte(),
        0x61, 0x64, 0xD9.toByte(), 0x01, 0xF8.toByte(), 0x81.toByte(), 0xD9.toByte(), 0x01,
        0xF7.toByte(), 0x61, 0x63,
    ),
)

/**
 * {_ "Fun": true, "Amt": -2} -> 0xbf6346756ef563416d7421ff
 */
class ListCborElementTest2 : AbstractListCborElementTest(
    CborBuilder().startMap().put("Fun", true).put("Amt", -2).end()
        .build(),
    byteArrayOf(
        0xbf.toByte(), 0x63, 0x46, 0x75, 0x6e, 0xf5.toByte(), 0x63,
        0x41, 0x6d, 0x74, 0x21, 0xff.toByte(),
    ),
)

/**
 * ["a", {_ "b": "c"}] -> 0x826161bf61626163ff
 */
class ListCborElementTest3 : AbstractListCborElementTest(
    CborBuilder().addArray().add("a").startMap().put("b", "c").end()
        .end().build(),
    byteArrayOf(
        0x82.toByte(), 0x61, 0x61, 0xbf.toByte(), 0x61, 0x62, 0x61,
        0x63, 0xff.toByte(),
    ),
)

/**
 * {_ "a": 1, "b": [_ 2, 3]} -> 0xbf61610161629f0203ffff
 */
class ListCborElementTest4 : AbstractListCborElementTest(
    CborBuilder().startMap().put("a", 1).put("b", 2).startArray("b")
        .add(2).add(3).end().end().build(),
    byteArrayOf(
        0xbf.toByte(), 0x61, 0x61, 0x01, 0x61, 0x62, 0x9f.toByte(),
        0x02, 0x03, 0xff.toByte(), 0xff.toByte(),
    ),
)

/**
 * [_ 1, 2, 3, 4, 5, 6,7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17,18, 19, 20, 21,
 * 22,23, 24, 25] ->
 * 0x9f0102030405060708090a0b0c0d0e0f101112131415161718181819ff
 */
class ListCborElementTest5 : AbstractListCborElementTest(
    CborBuilder().startArray().add(1).add(2).add(3).add(4).add(5).add(6)
        .add(7).add(8).add(9).add(10).add(11).add(12).add(13).add(14).add(15).add(16).add(17).add(18).add(19).add(20)
        .add(21).add(22).add(23).add(24).add(25).end().build(),
    byteArrayOf(
        0x9f.toByte(), 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
        0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x18,
        0x18, 0x19, 0xff.toByte(),
    ),
)

/**
 * [1, [_ 2, 3], [4,5]] -> 0x83019f0203ff820405
 */
class ListCborElementTest6 : AbstractListCborElementTest(
    CborBuilder().addArray().add(1).startArray().add(2).add(3).end()
        .addArray().add(4).add(5).end().end().build(),
    byteArrayOf(
        0x83.toByte(), 0x01, 0x9f.toByte(), 0x02, 0x03, 0xff.toByte(),
        0x82.toByte(), 0x04, 0x05,
    ),
)

/**
 * [1, [2, 3], [_ 4,5]] -> 0x83 01 82 02 03 9f 04 05 ff
 */
class ListCborElementTest7 : AbstractListCborElementTest(
    CborBuilder().addArray().add(1).addArray().add(2).add(3).end()
        .startArray().add(4).add(5).end().end().build(),
    byteArrayOf(
        0x83.toByte(), 0x01, 0x82.toByte(), 0x02, 0x03, 0x9f.toByte(),
        0x04, 0x05, 0xff.toByte(),
    ),
)

/**
 * [_ 1, [2, 3], [4,5]] -> 0x9f01820203820405ff
 */
class ListCborElementTest8 : AbstractListCborElementTest(
    CborBuilder().add(CborArray().apply { chunked = true }).add(1).addArray()
        .add(2).add(3).end().addArray().add(4).add(5).end().add(CborBreak).build(),
    byteArrayOf(
        0x9f.toByte(), 0x01, 0x82.toByte(), 0x02, 0x03, 0x82.toByte(),
        0x04, 0x05, 0xff.toByte(),
    ),
    false,
)

/**
 * [_ 1, [2, 3], [_ 4, 5]] -> 0x9f018202039f0405ffff
 */

class ListCborElementTest9 : AbstractListCborElementTest(
    CborBuilder().add(CborArray().apply { chunked = true }).add(1).addArray()
        .add(2).add(3).end().addArray().add(4).add(5).end().add(CborBreak).build(),
    byteArrayOf(
        0x9f.toByte(), 0x01, 0x82.toByte(), 0x02, 0x03, 0x82.toByte(),
        0x04, 0x05, 0xff.toByte(),
    ),
    false,
)

/**
 * [_ ] -> 0x9fff
 */
class ListCborElementTest10 : AbstractListCborElementTest(
    CborBuilder().add(CborArray().apply { chunked = true })
        .add(CborBreak).build(),
    byteArrayOf(0x9f.toByte(), 0xff.toByte()),
    false,
)

/**
 * (_ "strea", "ming") -> 0x7f657374726561646d696e67ff
 */
class ListCborElementTest11 : AbstractListCborElementTest(
    CborBuilder().startString().add("strea").add("ming").end().build(),
    byteArrayOf(
        0x7f, 0x65, 0x73, 0x74, 0x72, 0x65, 0x61, 0x64, 0x6d, 0x69,
        0x6e, 0x67, 0xff.toByte(),
    ),
    false,
)

/**
 * [1, [2, 3], [4, 5]] -> 0x83 01 82 02 03 82 04 05
 */
class ListCborElementTest12 : AbstractListCborElementTest(
    CborBuilder().addArray().add(1).addArray().add(2).add(3).end()
        .addArray().add(4).add(5).end().end().build(),
    byteArrayOf(
        0x83.toByte(),
        0x01,
        0x82.toByte(),
        0x02,
        0x03,
        0x82.toByte(),
        0x04,
        0x05,
    ),
)

/**
 * [1, 2, 3, 4, 5, 6,7, 8, 9, 10, 11, 12,13, 14, 15, 16, 17,18, 19, 20, 21, 22,
 * 23, 24, 25] -> 0x98 190102030405060708090a0b0c0d0e0f101112131415161718181819
 */
class ListCborElementTest13 : AbstractListCborElementTest(
    CborBuilder().addArray().add(1).add(2).add(3).add(4).add(5).add(6)
        .add(7).add(8).add(9).add(10).add(11).add(12).add(13).add(14).add(15).add(16).add(17).add(18).add(19).add(20)
        .add(21).add(22).add(23).add(24).add(25).end().build(),
    byteArrayOf(
        0x98.toByte(), 0x19, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06,
        0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18,
        0x18, 0x18, 0x19,
    ),
)

/**
 * {} -> 0xa0
 */
class ListCborElementTest14 : AbstractListCborElementTest(
    CborBuilder().addMap().end().build(),
    byteArrayOf(0xa0.toByte()),
)

/**
 * {1: 2, 3: 4} -> 0xa201020304
 */
class ListCborElementTest15 : AbstractListCborElementTest(
    CborBuilder().addMap().put(1, 2).put(3, 4).end().build(),
    byteArrayOf(0xa2.toByte(), 0x01, 0x02, 0x03, 0x04),
)

/**
 * {"a": 1, "b": [2,3]} -> 0xa26161016162820203
 */
class ListCborElementTest16 : AbstractListCborElementTest(
    CborBuilder().addMap().put("a", 1).putArray("b").add(2).add(3).end()
        .end().build(),
    byteArrayOf(
        0xa2.toByte(), 0x61, 0x61, 0x01, 0x61, 0x62, 0x82.toByte(),
        0x02, 0x03,
    ),
)

/**
 * ["a", {"b": "c"}] -> 0x826161a161626163
 */
class ListCborElementTest17 : AbstractListCborElementTest(
    CborBuilder().addArray().add("a").addMap().put("b", "c").end().end()
        .build(),
    byteArrayOf(
        0x82.toByte(),
        0x61,
        0x61,
        0xa1.toByte(),
        0x61,
        0x62,
        0x61,
        0x63,
    ),
)

/**
 * {"a": "A", "b": "B", "c": "C", "d": "D","e": "E"} ->
 * 0xa56161614161626142616361436164614461656145
 */
class ListCborElementTest18 : AbstractListCborElementTest(
    CborBuilder().addMap().put("a", "A").put("b", "B").put("c", "C")
        .put("d", "D").put("e", "E").end().build(),
    byteArrayOf(
        0xa5.toByte(), 0x61, 0x61, 0x61, 0x41, 0x61, 0x62, 0x61,
        0x42, 0x61, 0x63, 0x61, 0x43, 0x61, 0x64, 0x61, 0x44, 0x61, 0x65, 0x61, 0x45,
    ),
)

/**
 * (_ h'0102', h'030405') -> 0x5f42010243030405ff
 */
class ListCborElementTest19 : AbstractListCborElementTest(
    CborBuilder().startByteString().add(byteArrayOf(0x01, 0x02))
        .add(byteArrayOf(0x03, 0x04, 0x05)).end().build(),
    byteArrayOf(
        0x5f, 0x42, 0x01, 0x02, 0x43, 0x03, 0x04, 0x05,
        0xff.toByte(),
    ),
    false,
)

/**
 * 1(1363896240.5) -> 0xc1fb41d452d9ec200000
 */
class ListCborElementTest20 : AbstractListCborElementTest(
    CborBuilder().add(CborDoublePrecisionFloat(1363896240.5).apply { tag = CborTag(1) }).build(),
    byteArrayOf(
        0xc1.toByte(), 0xfb.toByte(), 0x41, 0xd4.toByte(), 0x52, 0xd9.toByte(),
        0xec.toByte(), 0x20, 0x00, 0x00,
    ),
)

/**
 * 23(h'01020304') -> 0xd74401020304
 */
class ListCborElementTest21 : AbstractListCborElementTest(
    CborBuilder().add(CborByteString(byteArrayOf(0x01, 0x02, 0x03, 0x04)).apply { tag = CborTag(23) }).build(),
    byteArrayOf(0xd7.toByte(), 0x44, 0x01, 0x02, 0x03, 0x04),
)

/**
 * 24(h'6449455446') -> 0xd818456449455446
 */
class ListCborElementTest22 : AbstractListCborElementTest(
    CborBuilder().add(CborByteString(byteArrayOf(0x64, 0x49, 0x45, 0x54, 0x46)).apply { tag = CborTag(24) }).build(),
    byteArrayOf(0xd8.toByte(), 0x18, 0x45, 0x64, 0x49, 0x45, 0x54, 0x46),
)

/**
 * 1(1363896240) -> 0xc11a514b67b0
 */
class ListCborElementTest23 : AbstractListCborElementTest(
    CborBuilder().add(CborUnsignedInteger(1363896240).apply { tag = CborTag(1) }).build(),
    byteArrayOf(0xc1.toByte(), 0x1a, 0x51, 0x4b, 0x67, 0xb0.toByte()),
)

/**
 * 0("2013-03-21T20:04:00Z") -> 0xc074323031332d30332d32315432303a30343a30305a
 */
class ListCborElementTest24 : AbstractListCborElementTest(
    CborBuilder().add(CborUnicodeString("2013-03-21T20:04:00Z").apply { tag = CborTag(0) }).build(),
    byteArrayOf(
        0xc0.toByte(), 0x74, 0x32, 0x30, 0x31, 0x33, 0x2d, 0x30, 0x33, 0x2d,
        0x32, 0x31, 0x54, 0x32, 0x30, 0x3a, 0x30, 0x34, 0x3a, 0x30, 0x30, 0x5a,
    ),
)

/**
 * 32("http://www.example.com") ->
 * 0xd82076687474703a2f2f7777772e6578616d706c652e636f6d
 */

class ListCborElementTest25 : AbstractListCborElementTest(
    CborBuilder().add(CborUnicodeString("http://www.example.com").apply { tag = CborTag(32) }).build(),
    byteArrayOf(
        0xd8.toByte(), 0x20, 0x76, 0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f,
        0x77, 0x77, 0x77, 0x2e, 0x65, 0x78, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x2e, 0x63, 0x6f, 0x6d,
    ),
)

/**
 * [] -> 0x80
 */
class ListCborElementTest26 : AbstractListCborElementTest(
    CborBuilder().addArray().end().build(),
    byteArrayOf(0x80.toByte()),
)

/**
 * [1, 2, 3] -> 0x83010203
 */
class ListCborElementTest27 : AbstractListCborElementTest(
    CborBuilder().addArray().add(1).add(2).add(3).end().build(),
    byteArrayOf(0x83.toByte(), 0x01, 0x02, 0x03),
)
