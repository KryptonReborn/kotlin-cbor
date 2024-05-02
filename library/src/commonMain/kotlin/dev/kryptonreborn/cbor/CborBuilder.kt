package dev.kryptonreborn.cbor

import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.kryptonreborn.cbor.builder.*
import dev.kryptonreborn.cbor.model.*

class CborBuilder(
    private val elements: MutableList<CborElement> = mutableListOf()
) : BaseBuilder<CborBuilder>(null) {
    fun reset(): CborBuilder {
        elements.clear()
        return this
    }

    fun build(): List<CborElement> {
        return elements
    }

    fun add(element: CborElement): CborBuilder {
        elements.add(element)
        return this
    }

    fun add(value: Long): CborBuilder {
        add(value.toCborElement())
        return this
    }

    fun add(value: BigInteger): CborBuilder {
        add(value.toCborElement())
        return this
    }

    fun add(value: Boolean): CborBuilder {
        add(value.toCborElement())
        return this
    }

    fun add(value: Float): CborBuilder {
        add(value.toCborElement())
        return this
    }

    fun add(value: Double): CborBuilder {
        add(value.toCborElement())
        return this
    }

    fun add(bytes: ByteArray?): CborBuilder {
        add(bytes.toCborElement())
        return this
    }

    fun startByteString(): CborByteStringBuilder<CborBuilder> {
        return startByteString(null)
    }

    fun startByteString(bytes: ByteArray?): CborByteStringBuilder<CborBuilder> {
        add(CborByteString(bytes).apply { chunked = true })
        return CborByteStringBuilder(this)
    }

    fun add(string: String?): CborBuilder {
        add(string.toCborElement())
        return this
    }

    fun startString(): CborUnicodeStringBuilder<CborBuilder> {
        return startString(null)
    }

    fun startString(string: String?): CborUnicodeStringBuilder<CborBuilder> {
        add(CborUnicodeString(string).apply { chunked = true })
        return CborUnicodeStringBuilder(this)
    }

    fun addTag(value: Long): CborBuilder {
        add(value.asCborTag())
        return this
    }

    fun tagged(value: Long): CborBuilder {
        val item: CborElement = elements.lastOrNull() ?: throw CborException("Can't add a tag before adding an item")
        item.getOuterTaggable().tag = value.asCborTag()
        return this
    }

    fun startArray(): CborArrayBuilder<CborBuilder> {
        val array = CborArray().apply { chunked = true }
        add(array)
        return CborArrayBuilder(array, this)
    }

    fun addArray(): CborArrayBuilder<CborBuilder> {
        val array = CborArray()
        add(array)
        return CborArrayBuilder(array, this)
    }

    fun addMap(): CborMapBuilder<CborBuilder> {
        val map = CborMap()
        add(map)
        return CborMapBuilder(map, this)
    }

    fun startMap(): CborMapBuilder<CborBuilder> {
        val map = CborMap().apply { chunked = true }
        add(map)
        return CborMapBuilder(map, this)
    }

    override fun addChunk(element: CborElement) {
        add(element)
    }
}