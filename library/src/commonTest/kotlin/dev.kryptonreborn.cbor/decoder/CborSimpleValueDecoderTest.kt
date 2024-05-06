package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborFalse
import dev.kryptonreborn.cbor.model.CborSimpleValue
import dev.kryptonreborn.cbor.model.CborSpecialElement
import dev.kryptonreborn.cbor.model.CborTrue
import dev.kryptonreborn.cbor.model.MajorType
import dev.kryptonreborn.cbor.model.SimpleValueType
import dev.kryptonreborn.cbor.model.SpecialType
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.readByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

class CborSimpleValueDecoderTest {
    @Test
    fun shouldDecodeBoolean() {
        val byteArrayOutputStream = Buffer()
        val encoder = CborEncoder(byteArrayOutputStream)
        encoder.encode(CborTrue)
        encoder.encode(CborFalse)
        val encodedBytes: ByteArray = byteArrayOutputStream.readByteArray()
        val source: Source = Buffer().apply { write(encodedBytes) }
        val cborElements: List<CborElement> = CborDecoder(source).decode()
        var result = 0
        var position = 1
        for (cborElement in cborElements) {
            position++
            when (cborElement.majorType) {
                MajorType.SPECIAL -> {
                    val special: CborSpecialElement = cborElement as CborSpecialElement
                    when (special.specialType) {
                        SpecialType.SIMPLE_VALUE -> {
                            val simpleValue: CborSimpleValue = special as CborSimpleValue
                            when (simpleValue.simpleValueType) {
                                SimpleValueType.FALSE -> result += position * 2
                                SimpleValueType.TRUE -> result += position * 3
                                else -> {}
                            }
                        }

                        else -> {}
                    }
                }

                else -> {}
            }
        }
        assertEquals(12, result.toLong())
    }
}
