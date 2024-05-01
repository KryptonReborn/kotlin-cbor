package dev.kryptonreborn.cbor

sealed class CborFloat(
    open val value: Float,
    specialType: SpecialType,
) : CborSpecialElement(specialType)

data class CborHalfPrecisionFloat(
    override val value: Float,
) : CborFloat(value, SpecialType.IEEE_754_HALF_PRECISION_FLOAT)

data class CborSinglePrecisionFloat(
    override val value: Float,
) : CborFloat(value, SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT)

data class CborDoublePrecisionFloat(
    val value: Double,
) : CborSpecialElement(SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT)
