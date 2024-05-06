package dev.kryptonreborn.cbor.model

sealed class CborChunkableElement(
    var chunked: Boolean = false,
    majorType: MajorType,
) : CborElement(majorType) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborChunkableElement) return false
        if (!super.equals(other)) return false

        if (chunked != other.chunked) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + chunked.hashCode()
        return result
    }
}
