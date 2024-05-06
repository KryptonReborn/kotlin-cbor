package dev.kryptonreborn.cbor.model

data class CborByteString(
    val bytes: ByteArray?,
) : CborChunkableElement(majorType = MajorType.BYTE_STRING) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborByteString) return false
        if (!super.equals(other)) return false

        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (bytes?.contentHashCode() ?: 0)
        return result
    }
}
