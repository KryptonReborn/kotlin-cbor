package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborElement
import kotlin.test.Test

class CborUnicodeStringEncoderTest {
    @Test
    fun shouldEncodeNullString() {
        val cborElements: List<CborElement> = CborBuilder().add(null as String?).build()
        CborEncoder.encodeToBytes(cborElements)
    }

    @Test
    fun shouldEncodeChunkedString() {
        val cborElements: List<CborElement> = CborBuilder().startString("test").end().build()
        CborEncoder.encodeToBytes(cborElements)
    }
}
