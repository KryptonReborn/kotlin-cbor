package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.*
import dev.kryptonreborn.cbor.SimpleValueType.*
import dev.kryptonreborn.cbor.SpecialType.*
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
            BREAK -> writeBytes(
                ((7 shl 5) or 31).toByte()
            )

            SIMPLE_VALUE -> {
                val simpleValue = data as CborSimpleValue
                when (simpleValue.simpleValueType) {
                    FALSE, NULL, TRUE, UNDEFINED -> {
                        writeBytes(
                            ((7 shl 5) or simpleValue.simpleValueType.value).toByte(),
                        )
                    }

                    UNALLOCATED -> writeBytes(
                        ((7 shl 5) or simpleValue.value).toByte(),
                    )

                    RESERVED -> {}
                }
            }

            IEEE_754_HALF_PRECISION_FLOAT -> halfPrecisionFloatEncoder.encode(data as CborHalfPrecisionFloat)
            IEEE_754_SINGLE_PRECISION_FLOAT -> singlePrecisionFloatEncoder.encode(data as CborSinglePrecisionFloat)
            IEEE_754_DOUBLE_PRECISION_FLOAT -> doublePrecisionFloatEncoder.encode(data as CborDoublePrecisionFloat)
            SIMPLE_VALUE_NEXT_BYTE -> {
                val simpleValueNextByte = data as CborSimpleValue
                writeBytes(
                    ((7 shl 5) or 24).toByte(),
                    simpleValueNextByte.value.toByte(),
                )
            }
        }
    }
}