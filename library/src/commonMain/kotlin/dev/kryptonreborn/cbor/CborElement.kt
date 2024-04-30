package dev.kryptonreborn.cbor

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
}
