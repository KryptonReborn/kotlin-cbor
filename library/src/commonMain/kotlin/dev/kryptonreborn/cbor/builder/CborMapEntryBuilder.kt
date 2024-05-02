package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.asCborTag
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.toCborElement

class CborMapEntryBuilder<T : CborMapBuilder<*>>(
    private val key: CborElement,
    parent: T,
) : BaseBuilder<T>(parent) {
    fun value(value: Boolean): T {
        return put(key, value.toCborElement())
    }

    fun value(value: ByteArray?): T {
        return put(key, value.toCborElement())
    }

    fun value(value: Double): T {
        return put(key, value.toCborElement())
    }

    fun value(value: String?): T {
        return put(key, value.toCborElement())
    }

    private fun put(key: CborElement, value: CborElement): T {
        parent!!.put(key, value)
        return parent
    }

    fun tagged(tag: Long): CborMapEntryBuilder<T> {
        val item = key.getOuterTaggable()
        item.tag = tag.asCborTag()
        return this
    }
}
