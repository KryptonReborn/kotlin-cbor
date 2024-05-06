package dev.kryptonreborn.cbor.model

sealed class CborFloat(
    open val value: Float,
    specialType: SpecialType,
) : CborSpecialElement(specialType) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborFloat) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

data class CborHalfPrecisionFloat(
    override val value: Float,
) : CborFloat(value, SpecialType.IEEE_754_HALF_PRECISION_FLOAT) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborHalfPrecisionFloat) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

data class CborSinglePrecisionFloat(
    override val value: Float,
) : CborFloat(value, SpecialType.IEEE_754_SINGLE_PRECISION_FLOAT) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborSinglePrecisionFloat) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}

data class CborDoublePrecisionFloat(
    val value: Double,
) : CborSpecialElement(SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborDoublePrecisionFloat) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}
