package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.toCborElement

class CborByteStringBuilder<T : BaseBuilder<*>>(
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(bytes: ByteArray?): CborByteStringBuilder<T> =
        this.apply {
            parent!!.addChunk(bytes.toCborElement())
        }

    fun end(): T {
        parent!!.addChunk(CborBreak)
        return parent
    }
}
