package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.*
import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.model.CborByteString
import dev.kryptonreborn.cbor.model.MajorType
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.readByteArray

class CborByteStringDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborByteString>(input, decoder) {

    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborByteString {
        val length = getLength(initialByte)
        return if (length == INFINITY) {
            if (decoder.autoDecodeInfinitiveByteStrings) {
                decodeInfinitiveLength()
            } else {
                CborByteString(null).apply {
                    chunked = true
                }
            }
        } else {
            decodeFixedLength(length)
        }
    }

    @Throws(CborException::class)
    private fun decodeInfinitiveLength(): CborByteString {
        val bytes = Buffer()
        while (true) {
            val item = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
            if (CborBreak == item) {
                break
            } else if (item.majorType === MajorType.BYTE_STRING) {
                (item as CborByteString).bytes?.let { byteArray ->
                    bytes.write(byteArray)
                }
            } else {
                throw CborException("Unexpected major type ${item.majorType}")
            }
        }
        return CborByteString(bytes.readByteArray())
    }

    @Throws(CborException::class)
    private fun decodeFixedLength(length: Long): CborByteString {
        return CborByteString(decodeBytes(length))
    }
}
