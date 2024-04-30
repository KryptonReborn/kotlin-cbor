package dev.kryptonreborn.cbor

sealed class CborChunkableElement(
    var chunked: Boolean = false,
    majorType: MajorType,
) : CborElement(majorType)
