package dev.kryptonreborn.cbor.model

data class CborUnicodeString(
    val string: String?,
) : CborChunkableElement(majorType = MajorType.UNICODE_STRING) {
    override fun toString(): String {
        return string ?: "null"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborUnicodeString) return false
        if (!super.equals(other)) return false

        if (string != other.string) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (string?.hashCode() ?: 0)
        return result
    }
}
