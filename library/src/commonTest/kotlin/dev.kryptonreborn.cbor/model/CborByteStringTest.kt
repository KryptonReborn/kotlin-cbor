package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CborByteStringTest : AbstractCborElementTest() {
    @Test
    fun testByteString() {
        shouldEncodeAndDecode("1-byte array", CborByteString(byteArrayOf(0x00.toByte())))
    }

    @Test
    fun testUnicodeString() {
        shouldEncodeAndDecode("string", CborUnicodeString("hello world"))
    }

    @Test
    fun shouldEquals() {
        val bytes: ByteArray = "string".encodeToByteArray()
        val byteString = CborByteString(bytes)
        assertEquals(byteString, byteString)
    }

    @Test
    fun shouldNotEquals() {
        val bytes: ByteArray = "string".encodeToByteArray()
        val byteString = CborByteString(bytes)
        assertNotEquals(byteString, Any())
    }

    @Test
    fun shouldNotClone() {
        val bytes: ByteArray = "see issue #18".encodeToByteArray()
        val byteString = CborByteString(bytes)
        assertEquals(byteString.bytes, bytes)
    }
}
