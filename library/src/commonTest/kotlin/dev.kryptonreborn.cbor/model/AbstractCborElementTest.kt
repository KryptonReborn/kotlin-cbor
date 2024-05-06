package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import kotlin.test.assertEquals

abstract class AbstractCborElementTest {
    protected fun shouldEncodeAndDecode(message: String, cborElement: CborElement) {
        val bytes: ByteArray = cborElement.encodeToBytes()
        val decoded: CborElement = CborDecoder.decode(bytes)[0]
        assertEquals(cborElement, decoded, message)
    }
}

fun CborElement.encodeToBytes(): ByteArray = CborEncoder.encodeToBytes(this)
