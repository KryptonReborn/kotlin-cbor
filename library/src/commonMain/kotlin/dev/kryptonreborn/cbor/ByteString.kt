package dev.kryptonreborn.cbor

data class ByteString(
    val bytes: ByteArray,
) : CborChunkableElement(majorType = MajorType.BYTE_STRING) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ByteString

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}
