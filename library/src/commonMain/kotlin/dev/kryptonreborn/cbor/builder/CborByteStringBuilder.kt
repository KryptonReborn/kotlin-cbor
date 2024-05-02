package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.toCborElement

class CborByteStringBuilder<T : BaseBuilder<*>>(
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(bytes: ByteArray?): CborByteStringBuilder<T> {
        parent!!.addChunk(bytes.toCborElement())
        return this
    }

    fun end(): T {
        parent!!.addChunk(CborBreak)
        return parent
    }
}
