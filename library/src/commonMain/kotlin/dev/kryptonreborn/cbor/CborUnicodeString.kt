package dev.kryptonreborn.cbor

data class CborUnicodeString(
    private val string: String,
) : CborChunkableElement(majorType = MajorType.UNICODE_STRING)
