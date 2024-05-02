package dev.kryptonreborn.cbor

import dev.kryptonreborn.cbor.model.MajorType.*
import dev.kryptonreborn.cbor.encoder.*
import dev.kryptonreborn.cbor.model.*
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.readByteArray


class CborEncoder(
    sink: Sink,
) {
    private val negativeIntegerEncoder = CborNegativeIntegerEncoder(sink, this)
    private val unsignedIntegerEncoder = CborUnsignedIntegerEncoder(sink, this)
    private val byteStringEncoder = CborByteStringEncoder(sink, this)
    private val unicodeStringEncoder = CborUnicodeStringEncoder(sink, this)
    private val arrayEncoder = CborArrayEncoder(sink, this)
    private val mapEncoder = CborMapEncoder(sink, this)
    private val tagEncoder = CborTagEncoder(sink, this)
    private val specialElementEncoder = CborSpecialElementEncoder(sink, this)

    var canonical = true

    companion object {
        /**
         * Encode a list of [DataItem]s, also known as a stream, to a byte array.
         *
         * @param dataItems a list of [DataItem]s
         */
        fun encodeToBytes(dataItems: List<CborElement>): ByteArray {
            val buffer = Buffer()
            val encoder = CborEncoder(buffer)
            encoder.encode(dataItems)
            return buffer.readByteArray()
        }

        /**
         * Encode a single [DataItem] to a byte array.
         *
         * @param dataItem the [DataItem] to encode. If null, encoder encodes a
         * [SimpleValue] NULL value.
         */
        fun encodeToBytes(dataItem: CborElement?): ByteArray {
            val buffer = Buffer()
            val encoder = CborEncoder(buffer)
            encoder.encode(dataItem)
            return buffer.readByteArray()
        }
    }

    /**
     * Encode a list of [DataItem]s, also known as a stream.
     *
     * @param dataItems a list of [DataItem]s
     * @throws CborException if the [DataItem]s could not be encoded or there
     * was an problem with the [OutputStream].
     */
    @Throws(CborException::class)
    fun encode(dataItems: List<CborElement?>) {
        for (dataItem in dataItems) {
            encode(dataItem)
        }
    }

    /**
     * Encode a single {@link DataItem}.
     *
     * @param dataItem the {@link DataItem} to encode. If null, encoder encodes a
     *                 {@link SimpleValue} NULL value.
     * @throws CborException if {@link DataItem} could not be encoded or there was
     *                       an problem with the {@link OutputStream}.
     */
    @Throws(CborException::class)
    fun encode(data: CborElement?) {
        val encodingData = data ?: CborNull
        if (encodingData.hasTag()) {
            encode(encodingData.tag)
        }

        when (encodingData.majorType) {
            UNSIGNED_INTEGER -> unsignedIntegerEncoder.encode(encodingData as CborUnsignedInteger)
            NEGATIVE_INTEGER -> negativeIntegerEncoder.encode(encodingData as CborNegativeInteger)
            BYTE_STRING -> byteStringEncoder.encode(encodingData as CborByteString)
            UNICODE_STRING -> unicodeStringEncoder.encode(encodingData as CborUnicodeString)
            ARRAY -> arrayEncoder.encode(encodingData as CborArray)
            MAP -> mapEncoder.encode(encodingData as CborMap)
            SPECIAL -> specialElementEncoder.encode(encodingData as CborSpecialElement)
            TAG -> tagEncoder.encode(encodingData as CborTag)
        }
    }
}