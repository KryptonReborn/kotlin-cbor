package dev.kryptonreborn.cbor.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

sealed class CborNumber(
    open val value: BigInteger,
    majorType: MajorType,
) : CborElement(majorType) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborNumber) return false
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

data class CborNegativeInteger(
    override val value: BigInteger,
) : CborNumber(value, MajorType.NEGATIVE_INTEGER) {
    constructor(value: Long) : this(value.toBigInteger())

    init {
        require(value.isNegative) { "value must be negative but was $value" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborNegativeInteger) return false
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

data class CborUnsignedInteger(
    override val value: BigInteger,
) : CborNumber(value, MajorType.UNSIGNED_INTEGER) {
    constructor(value: Long) : this(value.toBigInteger())

    init {
        require(!value.isNegative) { "value must not be negative but was $value" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborUnsignedInteger) return false
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
