package dev.kryptonreborn.cbor

import com.ionspin.kotlin.bignum.integer.BigInteger

open class CborArray(
    private val data: MutableList<CborElement> = mutableListOf(),
) : CborChunkableElement(majorType = MajorType.ARRAY) {
    fun items(): List<CborElement> = data.toList()

    fun add(element: CborElement): Boolean = data.add(element)

    fun get(index: Int): CborElement = data[index]

    fun remove(index: Int): CborElement = data.removeAt(index)
}

data class RationalNumber(
    val numerator: CborNumber,
    val denominator: CborNumber,
) : CborArray() {
    constructor(numerator: BigInteger, denominator: BigInteger) : this(
        if (numerator.isNegative) CborNegativeInteger(numerator) else CborUnsignedInteger(numerator),
        if (denominator.isNegative) CborNegativeInteger(denominator) else CborUnsignedInteger(denominator),
    )

    init {
        require(!denominator.value.isZero()) {
            "Denominator must be non-zero!"
        }
        tag = CborTag(30)
        add(numerator)
        add(denominator)
    }
}

data class LanguageTaggedString(
    val language: CborUnicodeString,
    val string: CborUnicodeString,
) : CborArray() {
    constructor(language: String, string: String) : this(CborUnicodeString(language), CborUnicodeString(string))

    init {
        tag = CborTag(38)
        add(language)
        add(string)
    }
}
