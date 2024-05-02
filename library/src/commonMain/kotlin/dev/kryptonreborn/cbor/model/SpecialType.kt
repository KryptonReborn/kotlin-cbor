package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborException

enum class SpecialType {
    SIMPLE_VALUE,
    SIMPLE_VALUE_NEXT_BYTE,
    IEEE_754_HALF_PRECISION_FLOAT,
    IEEE_754_SINGLE_PRECISION_FLOAT,
    IEEE_754_DOUBLE_PRECISION_FLOAT,
    BREAK;

    companion object {

        @Throws(CborException::class)
        fun ofByte(b: Int): SpecialType = when (b and 31) {
            24 -> SIMPLE_VALUE_NEXT_BYTE
            25 -> IEEE_754_HALF_PRECISION_FLOAT
            26 -> IEEE_754_SINGLE_PRECISION_FLOAT
            27 -> IEEE_754_DOUBLE_PRECISION_FLOAT
            28, 29, 30 -> throw CborException("Not implemented special type $b")
            31 -> BREAK
            else -> SIMPLE_VALUE
        }
    }
}