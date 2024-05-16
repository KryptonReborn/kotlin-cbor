package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborDoublePrecisionFloat
import dev.kryptonreborn.cbor.model.CborHalfPrecisionFloat
import dev.kryptonreborn.cbor.model.CborSimpleValue
import dev.kryptonreborn.cbor.model.CborSinglePrecisionFloat
import dev.kryptonreborn.cbor.model.CborSpecialElement
import dev.kryptonreborn.cbor.model.SimpleValueType
import dev.kryptonreborn.cbor.model.SpecialType
import kotlinx.io.Sink

class CborSpecialElementEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborSpecialElement>(sink, cborEncoder) {
    private val halfPrecisionFloatEncoder = CborHalfPrecisionFloatEncoder(sink, cborEncoder)
    private val singlePrecisionFloatEncoder = CborSinglePrecisionFloatEncoder(sink, cborEncoder)
    private val doublePrecisionFloatEncoder = CborDoublePrecisionFloatEncoder(sink, cborEncoder)

    override fun encode(data: CborSpecialElement) {
        when (data.specialType) {
            SpecialType.BREAK ->
                writeBytes(
                    ((7 shl 5) or 31).toByte(),
                )

            SpecialType.SIMPLE_VALUE -> {
                val simpleValue = data as CborSimpleValue
                when (simpleValue.simpleValueType) {
                    SimpleValueType.FALSE, SimpleValueType.NULL, SimpleValueType.TRUE, SimpleValueType.UNDEFINED ->
                        writeBytes(
                            ((7 shl 5) or simpleValue.simpleValueType.value).toByte(),
                        )

                    SimpleValueType.UNALLOCATED ->
                        writeBytes(
                            ((7 shl 5) or simpleValue.value).toByte(),
                        )

                    SimpleValueType.RESERVED -> {}
                }
            }

            SpecialType.IEEE_754_HALF_PRECISION_FLOAT -> halfPrecisionFloatEncoder.encode(data as CborHalfPrecisionFloat)
            SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT -> singlePrecisionFloatEncoder.encode(data as CborSinglePrecisionFloat)
            SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT -> doublePrecisionFloatEncoder.encode(data as CborDoublePrecisionFloat)
            SpecialType.SIMPLE_VALUE_NEXT_BYTE -> {
                val simpleValueNextByte = data as CborSimpleValue
                writeBytes(
                    ((7 shl 5) or 24).toByte(),
                    simpleValueNextByte.value.toByte(),
                )
            }
        }
    }
}
