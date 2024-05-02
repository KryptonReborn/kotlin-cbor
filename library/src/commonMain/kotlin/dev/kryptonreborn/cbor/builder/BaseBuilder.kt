package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.model.CborElement

abstract class BaseBuilder<T>(
    protected val parent: T?,
) {
    open fun addChunk(element: CborElement) {
        throw IllegalStateException()
    }
}
