package dev.kryptonreborn.cbor

data class CborByteString(
    val bytes: ByteArray?,
) : CborChunkableElement(majorType = MajorType.BYTE_STRING) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborByteString) return false

        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes?.contentHashCode() ?: 0
    }
}
