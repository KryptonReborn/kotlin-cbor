package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.asCborTag
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.model.lastOrNull
import dev.kryptonreborn.cbor.toCborElement

class CborArrayBuilder<T : BaseBuilder<*>>(
    private val array: CborArray,
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(element: CborElement): CborArrayBuilder<T> =
        this.apply {
            array.add(element)
        }

    fun add(value: Long): CborArrayBuilder<T> =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Boolean): CborArrayBuilder<T> =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Float): CborArrayBuilder<T> =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Double): CborArrayBuilder<T> =
        this.apply {
            add(value.toCborElement())
        }

    fun add(bytes: ByteArray?): CborArrayBuilder<T> =
        this.apply {
            add(bytes.toCborElement())
        }

    fun add(string: String?): CborArrayBuilder<T> =
        this.apply {
            add(string.toCborElement())
        }

    fun tagged(tag: Long): CborArrayBuilder<T> =
        this.apply {
            val item = array.lastOrNull() ?: throw CborException("Can't add a tag before adding an item")
            item.getOuterTaggable().tag = tag.asCborTag()
        }

    fun addArray(): CborArrayBuilder<CborArrayBuilder<T>> {
        val nestedArray = CborArray()
        add(nestedArray)
        return CborArrayBuilder(nestedArray, this)
    }

    fun startArray(): CborArrayBuilder<CborArrayBuilder<T>> {
        val nestedArray = CborArray().apply { chunked = true }
        add(nestedArray)
        return CborArrayBuilder(nestedArray, this)
    }

    fun addMap(): CborMapBuilder<CborArrayBuilder<T>> {
        val nestedMap = CborMap()
        add(nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun startMap(): CborMapBuilder<CborArrayBuilder<T>> {
        val nestedMap = CborMap().apply { chunked = true }
        add(nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun end(): T {
        if (array.chunked) {
            add(CborBreak)
        }
        return parent!!
    }
}
