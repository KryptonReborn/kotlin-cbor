package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.*
import dev.kryptonreborn.cbor.model.SimpleValueType.*
import dev.kryptonreborn.cbor.model.SpecialType.*
import kotlinx.io.Source

class CborSpecialElementDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborSpecialElement>(input, decoder) {
    private val halfPrecisionFloatDecoder = CborHalfPrecisionFloatDecoder(input, decoder)
    private val singlePrecisionFloatDecoder = CborSinglePrecisionFloatDecoder(input, decoder)
    private val doublePrecisionFloatDecoder = CborDoublePrecisionFloatDecoder(input, decoder)

    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborSpecialElement {
        return when (SpecialType.ofByte(initialByte)) {
            BREAK -> CborBreak
            SIMPLE_VALUE -> when (SimpleValueType.ofByte(initialByte)) {
                FALSE -> CborFalse
                TRUE -> CborTrue
                NULL -> CborNull
                UNDEFINED -> CborUndefined
                UNALLOCATED -> CborSimpleValue(initialByte and 31)
                RESERVED -> throw CborException("Not implemented")
            }

            IEEE_754_HALF_PRECISION_FLOAT -> halfPrecisionFloatDecoder.decode(initialByte)
            IEEE_754_SINGLE_PRECISION_FLOAT -> singlePrecisionFloatDecoder.decode(initialByte)
            IEEE_754_DOUBLE_PRECISION_FLOAT -> doublePrecisionFloatDecoder.decode(initialByte)
            SIMPLE_VALUE_NEXT_BYTE -> CborSimpleValue(nextSymbol())
        }
    }
}
