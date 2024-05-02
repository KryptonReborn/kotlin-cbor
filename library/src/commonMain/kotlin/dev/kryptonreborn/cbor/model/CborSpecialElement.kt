package dev.kryptonreborn.cbor.model

sealed class CborSpecialElement(
    val specialType: SpecialType,
) : CborElement(MajorType.SPECIAL)

data object CborBreak : CborSpecialElement(SpecialType.BREAK)

