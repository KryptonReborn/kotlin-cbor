package dev.kryptonreborn.cbor

import kotlin.test.Test
import kotlin.test.assertEquals

class CborEncoderTest {
    @Test
    fun shallEncode32bit() {
        val bytes: ByteArray = CborEncoder.encodeToBytes(CborBuilder().addTag(4294967296L).build())
        assertEquals(9, bytes.size.toLong())
        assertEquals(0xdB.toByte().toLong(), bytes[0].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[1].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[2].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[3].toLong())
        assertEquals(0x01.toByte().toLong(), bytes[4].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[5].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[6].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[7].toLong())
        assertEquals(0x00.toByte().toLong(), bytes[8].toLong())
    }
}
