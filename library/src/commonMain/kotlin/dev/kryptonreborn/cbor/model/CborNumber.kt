package dev.kryptonreborn.cbor.model

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger

sealed class CborNumber(
    open val value: BigInteger,
    majorType: MajorType,
) : CborElement(majorType)

data class CborNegativeInteger(
    override val value: BigInteger,
) : CborNumber(value, MajorType.NEGATIVE_INTEGER) {
    constructor(value: Long) : this(value.toBigInteger())

    init {
        require(value.isNegative) { "value must be negative but was $value" }
    }
}

data class CborUnsignedInteger(
    override val value: BigInteger,
) : CborNumber(value, MajorType.UNSIGNED_INTEGER) {
    constructor(value: Long) : this(value.toBigInteger())

    init {
        require(!value.isNegative) { "value must not be negative but was $value" }
    }
}
