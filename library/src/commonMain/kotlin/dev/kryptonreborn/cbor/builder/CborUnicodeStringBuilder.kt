package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.toCborElement

class CborUnicodeStringBuilder<T : BaseBuilder<*>>(
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(string: String?): CborUnicodeStringBuilder<T> =
        this.apply {
            parent!!.addChunk(string.toCborElement())
        }

    fun end(): T {
        parent!!.addChunk(CborBreak)
        return parent
    }
}
