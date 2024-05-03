package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborEncoder

sealed class CborElement(
    val majorType: MajorType,
    var tag: CborTag? = null,
) {
    fun hasTag(): Boolean {
        return tag != null
    }

    fun getOuterTaggable(): CborElement {
        var element = this
        while (element.hasTag()) {
            element = element.tag!!
        }
        return element
    }

    fun encodeToBytes(): ByteArray = CborEncoder.encodeToBytes(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborElement) return false

        if (majorType != other.majorType) return false
        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = majorType.hashCode()
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }
}
