package dev.kryptonreborn.cbor.model

data class CborUnicodeString(
    val string: String?,
) : CborChunkableElement(majorType = MajorType.UNICODE_STRING) {
    override fun toString(): String {
        return string ?: "null"
    }
}
