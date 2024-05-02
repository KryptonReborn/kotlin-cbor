package dev.kryptonreborn.cbor.builder

import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.asCborTag
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.toCborElement

class CborMapBuilder<T : BaseBuilder<*>>(
    private val map: CborMap,
    parent: T,
) : BaseBuilder<T>(parent) {
    private var lastItem: CborElement? = null

    fun put(key: CborElement, value: CborElement): CborMapBuilder<T> {
        map.put(key, value)
        lastItem = value
        return this
    }

    fun put(key: Long, value: Long): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: Long, value: Boolean): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: Long, value: Float): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: Long, value: Double): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: Long, value: ByteArray?): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: Long, value: String?): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: Long): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: Boolean): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: Float): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: Double): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: ByteArray?): CborMapBuilder<T> {
        map.put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun put(key: String, value: String?): CborMapBuilder<T> {
        put(key.toCborElement(), value.toCborElement())
        return this
    }

    fun putArray(key: CborElement): CborArrayBuilder<CborMapBuilder<T>> {
        val array = CborArray()
        put(key, array)
        return CborArrayBuilder(array, this)
    }

    fun putArray(key: Long): CborArrayBuilder<CborMapBuilder<T>> {
        val array = CborArray()
        put(key.toCborElement(), array)
        return CborArrayBuilder(array, this)
    }

    fun putArray(key: String): CborArrayBuilder<CborMapBuilder<T>> {
        val array = CborArray()
        put(key.toCborElement(), array)
        return CborArrayBuilder(array, this)
    }

    fun startArray(key: CborElement): CborArrayBuilder<CborMapBuilder<T>> {
        val array = CborArray().apply {
            chunked = true
        }
        put(key, array)
        return CborArrayBuilder(array, this)
    }

    fun startArray(key: Long): CborArrayBuilder<CborMapBuilder<T>> {
        return startArray(key.toCborElement())
    }

    fun startArray(key: String): CborArrayBuilder<CborMapBuilder<T>> {
        val array = CborArray().apply {
            chunked = true
        }
        put(key.toCborElement(), array)
        return CborArrayBuilder(array, this)
    }

    fun putMap(key: CborElement): CborMapBuilder<CborMapBuilder<T>> {
        val nestedMap = CborMap()
        put(key, nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun putMap(key: Long): CborMapBuilder<CborMapBuilder<T>> {
        val nestedMap = CborMap()
        put(key.toCborElement(), nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun putMap(key: String): CborMapBuilder<CborMapBuilder<T>> {
        val nestedMap = CborMap()
        put(key.toCborElement(), nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun startMap(key: CborElement): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key, true)
    }

    private fun startMap(key: CborElement, chunked: Boolean): CborMapBuilder<CborMapBuilder<T>> {
        val nestedMap = CborMap().apply {
            this.chunked = chunked
        }
        put(key, nestedMap)
        return CborMapBuilder(nestedMap, this)
    }

    fun startMapNotChunked(key: CborElement): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key, false)
    }

    fun tagged(tag: Long): CborMapBuilder<T> {
        lastItem?.let { it.getOuterTaggable().tag = tag.asCborTag() }
            ?: throw CborException("Can't add a tag before adding an item")
        return this
    }

    fun startMap(key: Long): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key.toCborElement())
    }

    fun startMap(key: String?): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key.toCborElement())
    }

    fun startMapNotChunked(key: Long): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key.toCborElement(), false)
    }

    fun startMapNotChunked(key: String?): CborMapBuilder<CborMapBuilder<T>> {
        return startMap(key.toCborElement(), false)
    }

    fun end(): T {
        return parent!!
    }

    fun addKey(key: Long): CborMapEntryBuilder<CborMapBuilder<T>> {
        return CborMapEntryBuilder(key.toCborElement(), this)
    }

    fun addKey(key: String): CborMapEntryBuilder<CborMapBuilder<T>> {
        return CborMapEntryBuilder(key.toCborElement(), this)
    }
}