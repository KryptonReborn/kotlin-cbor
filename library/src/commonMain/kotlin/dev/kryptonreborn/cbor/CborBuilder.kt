package dev.kryptonreborn.cbor

import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.kryptonreborn.cbor.builder.BaseBuilder
import dev.kryptonreborn.cbor.builder.CborArrayBuilder
import dev.kryptonreborn.cbor.builder.CborByteStringBuilder
import dev.kryptonreborn.cbor.builder.CborMapBuilder
import dev.kryptonreborn.cbor.builder.CborUnicodeStringBuilder
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.model.CborUnicodeString

class CborBuilder(
    private val elements: MutableList<CborElement> = mutableListOf(),
) : BaseBuilder<CborBuilder>(null) {
    fun reset(): CborBuilder =
        this.apply {
            elements.clear()
        }

    fun build(): List<CborElement> = elements.toList()

    fun add(element: CborElement): CborBuilder =
        this.apply {
            elements.add(element)
        }

    fun add(value: Long): CborBuilder =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: BigInteger): CborBuilder =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Boolean): CborBuilder =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Float): CborBuilder =
        this.apply {
            add(value.toCborElement())
        }

    fun add(value: Double): CborBuilder =
        this.apply {
            add(value.toCborElement())
        }

    fun add(bytes: ByteArray?): CborBuilder =
        this.apply {
            add(bytes.toCborElement())
        }

    fun startByteString(): CborByteStringBuilder<CborBuilder> = startByteString(null)

    fun startByteString(bytes: ByteArray?): CborByteStringBuilder<CborBuilder> {
        add(CborByteString(bytes).apply { chunked = true })
        return CborByteStringBuilder(this)
    }

    fun add(string: String?): CborBuilder =
        this.apply {
            add(string.toCborElement())
        }

    fun startString(): CborUnicodeStringBuilder<CborBuilder> = startString(null)

    fun startString(string: String?): CborUnicodeStringBuilder<CborBuilder> {
        add(CborUnicodeString(string).apply { chunked = true })
        return CborUnicodeStringBuilder(this)
    }

    fun addTag(value: Long): CborBuilder =
        this.apply {
            add(value.asCborTag())
        }

    fun tagged(value: Long): CborBuilder =
        this.apply {
            val item = elements.lastOrNull() ?: throw CborException("Can't add a tag before adding an item")
            item.getOuterTaggable().tag = value.asCborTag()
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
