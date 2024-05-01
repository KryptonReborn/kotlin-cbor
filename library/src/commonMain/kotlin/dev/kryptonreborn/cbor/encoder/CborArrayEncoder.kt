package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborArray
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink

class CborArrayEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborArray>(sink, cborEncoder) {
    override fun encode(data: CborArray) {
        if (data.chunked) {
            writeIndefiniteLengthType(MajorType.ARRAY)
        } else {
            writeType(MajorType.ARRAY, data.size().toLong())
        }
        data.items().forEach { item -> encoder.encode(item) }
    }
}
