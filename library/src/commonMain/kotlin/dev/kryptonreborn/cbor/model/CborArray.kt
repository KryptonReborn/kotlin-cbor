package dev.kryptonreborn.cbor.model

import com.ionspin.kotlin.bignum.integer.BigInteger

open class CborArray(
    private val data: MutableList<CborElement> = mutableListOf(),
) : CborChunkableElement(majorType = MajorType.ARRAY) {
    fun items(): List<CborElement> = data.toList()

    fun add(element: CborElement): Boolean = data.add(element)

    fun get(index: Int): CborElement = data[index]

    fun remove(index: Int): CborElement = data.removeAt(index)

    fun size(): Int = data.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborArray) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}

fun CborArray.firstOrNull(): CborElement? {
    return if (size() > 0) get(0) else null
}

fun CborArray.lastOrNull(): CborElement? {
    return if (size() > 0) get(size() - 1) else null
}

data class CborRationalNumber(
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
        tag = CborTag(TAG_VALUE)
        add(numerator)
        add(denominator)
    }

    companion object {
        const val TAG_VALUE = 30L
    }
}

data class CborLanguageTaggedString(
    val language: CborUnicodeString,
    val string: CborUnicodeString,
) : CborArray() {
    constructor(language: String, string: String) : this(CborUnicodeString(language), CborUnicodeString(string))

    init {
        tag = CborTag(TAG_VALUE)
        add(language)
        add(string)
    }

    companion object {
        const val TAG_VALUE = 38L
    }
}
