package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.model.CborFalse
import dev.kryptonreborn.cbor.model.CborNull
import dev.kryptonreborn.cbor.model.CborSimpleValue
import dev.kryptonreborn.cbor.model.CborSpecialElement
import dev.kryptonreborn.cbor.model.CborTrue
import dev.kryptonreborn.cbor.model.CborUndefined
import dev.kryptonreborn.cbor.model.SimpleValueType
import dev.kryptonreborn.cbor.model.SpecialType
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
            SpecialType.BREAK -> CborBreak
            SpecialType.SIMPLE_VALUE ->
                when (SimpleValueType.ofByte(initialByte)) {
                    SimpleValueType.FALSE -> CborFalse
                    SimpleValueType.TRUE -> CborTrue
                    SimpleValueType.NULL -> CborNull
                    SimpleValueType.UNDEFINED -> CborUndefined
                    SimpleValueType.UNALLOCATED -> CborSimpleValue(initialByte and 31)
                    SimpleValueType.RESERVED -> throw CborException("Not implemented")
                }

            SpecialType.IEEE_754_HALF_PRECISION_FLOAT ->
                halfPrecisionFloatDecoder.decode(
                    initialByte,
                )

            SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT ->
                singlePrecisionFloatDecoder.decode(
                    initialByte,
                )

            SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT ->
                doublePrecisionFloatDecoder.decode(
                    initialByte,
                )

            SpecialType.SIMPLE_VALUE_NEXT_BYTE -> CborSimpleValue(nextSymbol())
        }
    }
}
