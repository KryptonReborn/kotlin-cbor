package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.toCborElement

class CborUnicodeStringBuilder<T : BaseBuilder<*>>(
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(string: String?): CborUnicodeStringBuilder<T> {
        parent!!.addChunk(string.toCborElement())
        return this
    }

    fun end(): T {
        parent!!.addChunk(CborBreak)
        return parent
    }
}
