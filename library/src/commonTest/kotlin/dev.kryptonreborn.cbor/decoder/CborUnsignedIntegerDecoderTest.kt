package dev.kryptonreborn.cbor.decoder

import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.kryptonreborn.cbor.CborBuilder
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CborUnsignedIntegerDecoderTest {
    @Test
    fun shouldDecodeBigNumbers() {
        var value = BigInteger.ONE
        for (i in 1..63) {
            value = value.shl(1)
            val encodedBytes: ByteArray = CborEncoder.encodeToBytes(CborBuilder().add(value).build())
            val source: Source = Buffer().apply { write(encodedBytes) }
            val decoder = CborDecoder(source)
            val cborElements: List<CborElement> = decoder.decode()
            assertNotNull(cborElements)
            assertEquals(1, cborElements.size.toLong())
            val cborElement: CborElement = cborElements[0]
            assertTrue(cborElement is CborUnsignedInteger)
            assertEquals(value, cborElement.value)
        }
    }
}
