package dev.kryptonreborn.cbor

data class CborTag(
    val value: Long,
) : CborElement(MajorType.TAG)
