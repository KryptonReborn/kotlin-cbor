package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CborArrayDecoderTest {
    @Test
    fun shouldThrowOnIncompleteArray() {
        val bytes = byteArrayOf(0x82.toByte(), 0x01)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }
    }

    @Test
    fun shouldThrowInIncompleteIndefiniteLengthArray() {
        val bytes = byteArrayOf(0x9f.toByte(), 0x01, 0x02)
        assertFailsWith<CborException> { CborDecoder.decode(bytes) }
    }
}
