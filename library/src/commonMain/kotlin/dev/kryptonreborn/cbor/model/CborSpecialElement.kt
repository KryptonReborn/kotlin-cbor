package dev.kryptonreborn.cbor.model

sealed class CborSpecialElement(
    val specialType: SpecialType,
) : CborElement(MajorType.SPECIAL) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborSpecialElement) return false
        if (!super.equals(other)) return false

        if (specialType != other.specialType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + specialType.hashCode()
        return result
    }
}

data object CborBreak : CborSpecialElement(SpecialType.BREAK)
