package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborElement
import kotlin.test.Test

class CborMapEncoderTest {
    @Test
    fun shouldEncodeMap() {
        val cborElements: List<CborElement> =
            CborBuilder().addMap().put(1, true).put(".", true).put(3, true).put("..", true)
                .put(2, true).put("...", true).end().build()
        CborEncoder.encodeToBytes(cborElements)
    }
}
