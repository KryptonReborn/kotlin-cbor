package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.*
import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.model.CborUnicodeString
import dev.kryptonreborn.cbor.model.MajorType
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.readByteArray

class CborUnicodeStringDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborUnicodeString>(input, decoder) {

    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborUnicodeString {
        val length = getLength(initialByte)
        return if (length == INFINITY) {
            if (decoder.autoDecodeInfinitiveUnicodeStrings) {
                decodeInfinitiveLength()
            } else {
                CborUnicodeString(null).apply {
                    chunked = true
                }
            }
        } else {
            decodeFixedLength(length)
        }
    }

    @Throws(CborException::class)
    private fun decodeInfinitiveLength(): CborUnicodeString {
        val bytes = Buffer()
        while (true) {
            val item = decoder.decodeNext() ?: throw CborException("Unexpected end of stream")
            if (CborBreak == item) {
                break
            } else if (item.majorType === MajorType.UNICODE_STRING) {
                val byteArray = (item as CborUnicodeString).toString().encodeToByteArray()
                bytes.write(byteArray, 0, byteArray.size)
            } else {
                throw CborException("Unexpected major type ${item.majorType}")
            }
        }
        return CborUnicodeString(bytes.readByteArray().decodeToString())
    }

    @Throws(CborException::class)
    private fun decodeFixedLength(length: Long): CborUnicodeString {
        return CborUnicodeString(decodeBytes(length).decodeToString())
    }
}
