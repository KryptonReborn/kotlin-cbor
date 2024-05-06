package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.CborElement
import kotlin.test.Test

class CborByteStringEncoderTest {
    @Test
    fun shouldEncodeChunkedString() {
        val cborElements: List<CborElement> =
            CborBuilder().add(CborByteString(null)).add(CborByteString("test".encodeToByteArray()))
                .startByteString("test".encodeToByteArray()).end().build()
        CborEncoder.encodeToBytes(cborElements)
    }
}
