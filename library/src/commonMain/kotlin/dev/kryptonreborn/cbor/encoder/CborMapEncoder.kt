package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.model.CborBreak
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborMap
import dev.kryptonreborn.cbor.model.MajorType
import kotlinx.io.Buffer
import kotlinx.io.Sink
import kotlinx.io.readByteArray

class CborMapEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborMap>(sink, cborEncoder) {

    override fun encode(data: CborMap) {
        if (data.chunked) {
            writeIndefiniteLengthType(MajorType.MAP)
        } else {
            writeType(MajorType.MAP, data.keys().size.toLong())
        }

        if (data.keys().isEmpty()) {
            return
        }

        if (data.chunked) {
            writeNonCanonicalMap(data)
            encoder.encode(CborBreak)
        } else {
            if (encoder.canonical) {
                writeCanonicalMap(data)
            } else {
                writeNonCanonicalMap(data)
            }
        }

    }

    private fun writeNonCanonicalMap(map: CborMap) {
        for (key in map.keys()) {
            encoder.encode(key)
            encoder.encode(map.get(key))
        }
    }

    /**
     * Writes the map in the canonical CBOR format.
     *
     *
     * From [RFC 7049](https://tools.ietf.org/html/rfc7049#section-3.9):
     *
     *
     * The keys in every map must be sorted lowest value to highest. Sorting is performed on the
     * bytes of the representation of the key data items without paying attention to the 3/5 bit
     * splitting for major types. (Note that this rule allows maps that have keys of different
     * types, even though that is probably a bad practice that could lead to errors in some
     * canonicalization implementations.) The sorting rules are:
     *
     *
     * If two keys have different lengths, the shorter one sorts earlier;
     *
     *
     * If two keys have the same length, the one with the lower value in (byte-wise) lexical order
     * sorts earlier.
     */
    private fun writeCanonicalMap(map: CborMap) {
        val comparator = object : Comparator<MutableMap.MutableEntry<ByteArray, ByteArray>> {
            override fun compare(
                a: MutableMap.MutableEntry<ByteArray, ByteArray>,
                b: MutableMap.MutableEntry<ByteArray, ByteArray>
            ): Int {
                if (a.key.size < b.key.size) {
                    return -1
                }
                if (a.key.size > b.key.size) {
                    return 1
                }
                for (i in a.key.indices) {
                    if (a.key[i] < b.key[i]) {
                        return -1
                    }
                    if (a.key[i] > b.key[i]) {
                        return 1
                    }
                }
                return 0
            }
        }
        val encodingMap = mutableMapOf<ByteArray, ByteArray>()
        val buffer = Buffer()
        val mapEncoder = CborEncoder(buffer)
        for (key in map.keys()) {
            // Key
            mapEncoder.encode(key)
            val keyBytes = buffer.readByteArray()
            buffer.clear()
            // Value
            mapEncoder.encode(map.get(key))
            val valueBytes = buffer.readByteArray()
            buffer.clear()
            encodingMap[keyBytes] = valueBytes
        }

        for ((key, value) in encodingMap.entries.sortedWith(comparator)) {
            writeBytes(*key)
            writeBytes(*value)
        }
    }
}
