package dev.kryptonreborn.cbor.model

data class CborTag(
    val value: Long,
) : CborElement(MajorType.TAG)
