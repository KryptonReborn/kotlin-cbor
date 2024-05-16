package dev.kryptonreborn.cbor

import dev.kryptonreborn.cbor.decoder.CborArrayDecoder
import dev.kryptonreborn.cbor.decoder.CborByteStringDecoder
import dev.kryptonreborn.cbor.decoder.CborMapDecoder
import dev.kryptonreborn.cbor.decoder.CborNegativeIntegerDecoder
import dev.kryptonreborn.cbor.decoder.CborSpecialElementDecoder
import dev.kryptonreborn.cbor.decoder.CborTagDecoder
import dev.kryptonreborn.cbor.decoder.CborUnicodeStringDecoder
import dev.kryptonreborn.cbor.decoder.CborUnsignedIntegerDecoder
import dev.kryptonreborn.cbor.model.CborArray
import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborLanguageTaggedString
import dev.kryptonreborn.cbor.model.CborNumber
import dev.kryptonreborn.cbor.model.CborRationalNumber
import dev.kryptonreborn.cbor.model.CborUnicodeString
import dev.kryptonreborn.cbor.model.MajorType
import dev.kryptonreborn.cbor.model.MajorType.UNSIGNED_INTEGER
import kotlinx.io.Buffer
import kotlinx.io.Source
import kotlinx.io.readUByte

class CborDecoder(
    private val source: Source,
) {
    private val negativeIntegerDecoder = CborNegativeIntegerDecoder(source, this)
    private val unsignedIntegerDecoder = CborUnsignedIntegerDecoder(source, this)
    private val byteStringDecoder = CborByteStringDecoder(input = source, decoder = this)
    private val unicodeStringDecoder = CborUnicodeStringDecoder(source, this)
    private val arrayDecoder = CborArrayDecoder(source, this)
    private val mapDecoder = CborMapDecoder(input = source, decoder = this)
    private val tagDecoder = CborTagDecoder(source, this)
    private val specialElementDecoder = CborSpecialElementDecoder(source, this)

    var autoDecodeInfinitiveArrays = true
    var autoDecodeInfinitiveMaps = true
    var autoDecodeInfinitiveByteStrings = true
    var autoDecodeInfinitiveUnicodeStrings = true
    var autoDecodeRationalNumbers = true
    var autoDecodeLanguageTaggedStrings = true
    var rejectDuplicateKeys = false

    companion object {
        /**
         * Convenience method to decode a byte array directly.
         *
         * @param bytes the CBOR encoded data
         * @return a list of [CborElement]s
         * @throws CborException if decoding failed
         */
        @Throws(CborException::class)
        fun decode(bytes: ByteArray): List<CborElement> {
            return CborDecoder(Buffer().apply { write(bytes) }).decode()
        }
    }

    /**
     * Decode the [Source] to a list of [CborElement]s.
     *
     * @return the list of [CborElement]s
     * @throws CborException if decoding failed
     */
    @Throws(CborException::class)
    fun decode(): List<CborElement> {
        val elements = mutableListOf<CborElement>()
        var element: CborElement?
        while ((decodeNext().also { element = it }) != null) {
            elements.add(element!!)
        }
        return elements
    }

    /**
     * Decodes exactly one CborElement from the input stream.
     *
     * @return a [CborElement] or null if end of stream has reached.
     * @throws CborException if decoding failed
     */
    @Throws(CborException::class)
    fun decodeNext(): CborElement? {
        val symbol =
            try {
                source.readUByte().toInt()
            } catch (e: Exception) {
                return null
            }
        when (MajorType.ofByte(symbol)) {
            UNSIGNED_INTEGER -> return unsignedIntegerDecoder.decode(symbol)
            MajorType.NEGATIVE_INTEGER -> return negativeIntegerDecoder.decode(symbol)
            MajorType.BYTE_STRING -> return byteStringDecoder.decode(symbol)
            MajorType.UNICODE_STRING -> return unicodeStringDecoder.decode(symbol)
            MajorType.ARRAY -> return arrayDecoder.decode(symbol)
            MajorType.MAP -> return mapDecoder.decode(symbol)
            MajorType.SPECIAL -> return specialElementDecoder.decode(symbol)
            MajorType.TAG -> {
                val tag = tagDecoder.decode(symbol)
                val next =
                    decodeNext()
                        ?: throw CborException("Unexpected end of stream: tag without following data item.")

                if (autoDecodeRationalNumbers && tag.value == CborRationalNumber.TAG_VALUE) {
                    return decodeRationalNumber(next)
                } else if (autoDecodeLanguageTaggedStrings && tag.value == CborLanguageTaggedString.TAG_VALUE) {
                    return decodeLanguageTaggedString(next)
                } else {
                    var itemToTag = next
                    while (itemToTag.hasTag()) itemToTag = itemToTag.tag!!
                    itemToTag.tag = tag
                    return next
                }
            }

            else -> throw CborException("Not implemented major type $symbol")
        }
    }

    @Throws(CborException::class)
    private fun decodeRationalNumber(item: CborElement): CborRationalNumber {
        if (item !is CborArray) {
            throw CborException("Error decoding RationalNumber: not an array")
        }
        if (item.items().size != 2) {
            throw CborException("Error decoding RationalNumber: array size is not 2")
        }
        val numerator =
            item.get(0) as? CborNumber
                ?: throw CborException("Error decoding RationalNumber: first data item is not a number")
        val denominator =
            item.get(1) as? CborNumber
                ?: throw CborException("Error decoding RationalNumber: second data item is not a number")
        return CborRationalNumber(numerator, denominator)
    }

    @Throws(CborException::class)
    private fun decodeLanguageTaggedString(item: CborElement): CborLanguageTaggedString {
        if (item !is CborArray) {
            throw CborException("Error decoding LanguageTaggedString: not an array")
        }
        if (item.items().size != 2) {
            throw CborException("Error decoding LanguageTaggedString: array size is not 2")
        }
        val language =
            item.get(0) as? CborUnicodeString
                ?: throw CborException("Error decoding LanguageTaggedString: first data item is not an UnicodeString")
        val string =
            item.get(1) as? CborUnicodeString
                ?: throw CborException("Error decoding LanguageTaggedString: second data item is not an UnicodeString")
        return CborLanguageTaggedString(language, string)
    }
}
