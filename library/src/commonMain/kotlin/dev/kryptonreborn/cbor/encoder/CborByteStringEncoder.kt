package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborByteString
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborNull
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink

class CborByteStringEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborByteString>(sink, cborEncoder) {
    override fun encode(data: CborByteString) {
        if (data.chunked) {
            writeIndefiniteLengthType(MajorType.BYTE_STRING)
            if (data.bytes == null) {
                return
            }
        } else if (data.bytes == null) {
            encoder.encode(CborNull)
            return
        }
        writeType(MajorType.BYTE_STRING, data.bytes.size.toLong())
        writeBytes(*data.bytes)
    }
}