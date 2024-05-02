package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.*
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.model.CborElement
import kotlinx.io.Source

class CborArrayDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborArray>(input, decoder) {

    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborArray {
        val length = getLength(initialByte)
        return if (length == INFINITY) {
            decodeInfinitiveLength()
        } else {
            decodeFixedLength(length)
        }
    }

    @Throws(CborException::class)
    private fun decodeInfinitiveLength(): CborArray {
        val array = CborArray().apply {
            chunked = true
        }

        if (decoder.autoDecodeInfinitiveArrays) {
            var item: CborElement
            while (true) {
                item = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
                array.add(item)
                if (CborBreak == item) {
                    break
                }
            }
        }
        return array
    }

    @Throws(CborException::class)
    private fun decodeFixedLength(length: Long): CborArray {
        val array = CborArray()
        for (i in 0 until length) {
            val item = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
            array.add(item)
        }
        return array
    }
}
