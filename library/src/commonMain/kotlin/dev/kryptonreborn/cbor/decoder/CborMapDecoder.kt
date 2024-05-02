package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborMap
import kotlinx.io.Source

class CborMapDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborMap>(input, decoder) {

    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborMap {
        val length = getLength(initialByte)
        return if (length == INFINITY) {
            decodeInfinitiveLength()
        } else {
            decodeFixedLength(length)
        }
    }

    @Throws(CborException::class)
    private fun decodeInfinitiveLength(): CborMap {
        val map = CborMap().apply {
            chunked = true
        }
        if (decoder.autoDecodeInfinitiveMaps) {
            while (true) {
                val key = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
                if (CborBreak == key) {
                    break
                }
                val value = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
                if (decoder.rejectDuplicateKeys && map.get(key) != null) {
                    throw CborException("Duplicate key found in map")
                }
                map.put(key, value)
            }
        }
        return map
    }

    @Throws(CborException::class)
    private fun decodeFixedLength(length: Long): CborMap {
        val map = CborMap()
        for (i in 0 until length) {
            val key = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
            val value = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
            if (decoder.rejectDuplicateKeys && map.get(key) != null) {
                throw CborException("Duplicate key found in map")
            }
            map.put(key, value)
        }
        return map
    }
}