package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.asCborTag
import dev.kryptonreborn.cbor.model.*
import dev.kryptonreborn.cbor.toCborElement

class CborArrayBuilder<T : BaseBuilder<*>>(
    private val array: CborArray,
    parent: T,
) : BaseBuilder<T>(parent) {
    fun add(dataItem: CborElement): CborArrayBuilder<T> {
        array.add(dataItem)
        return this
    }

    fun add(value: Long): CborArrayBuilder<T> {
        add(value.toCborElement())
        return this
    }

    fun add(value: Boolean): CborArrayBuilder<T> {
        add(value.toCborElement())
        return this
    }

    fun add(value: Float): CborArrayBuilder<T> {
        add(value.toCborElement())
        return this
    }

    fun add(value: Double): CborArrayBuilder<T> {
        add(value.toCborElement())
        return this
    }

    fun add(bytes: ByteArray?): CborArrayBuilder<T> {
        add(bytes.toCborElement())
        return this
    }

    fun add(string: String?): CborArrayBuilder<T> {
        add(string.toCborElement())
        return this
    }

    fun tagged(tag: Long): CborArrayBuilder<T> {
        val item = array.lastOrNull() ?: throw CborException("Can't add a tag before adding an item")
        item.getOuterTaggable().tag = tag.asCborTag()
        return this
    }

    fun addArray(): CborArrayBuilder<CborArrayBuilder<T>> {
        val nestedArray = CborArray()
        add(nestedArray)
        return CborArrayBuilder(nestedArray, this)
    }

    fun startArray(): CborArrayBuilder<CborArrayBuilder<T>> {
        val nestedArray = CborArray().apply {
            chunked = true
        }
        add(nestedArray)
        return CborArrayBuilder(nestedArray, this)
    }

    fun addMap(): CborMapBuilder<CborArrayBuilder<T>> {
        val nestedMap = CborMap()
        add(nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun startMap(): CborMapBuilder<CborArrayBuilder<T>> {
        val nestedMap = CborMap().apply {
            chunked = true
        }
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