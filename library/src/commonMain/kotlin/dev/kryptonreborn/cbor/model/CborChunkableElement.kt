package dev.kryptonreborn.cbor.model

sealed class CborChunkableElement(
    var chunked: Boolean = false,
    majorType: MajorType,
) : CborElement(majorType)
