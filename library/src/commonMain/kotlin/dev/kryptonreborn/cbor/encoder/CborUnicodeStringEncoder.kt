package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborNull
import dev.kryptonreborn.cbor.CborUnicodeString
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink

class CborUnicodeStringEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborUnicodeString>(sink, cborEncoder) {
    override fun encode(data: CborUnicodeString) {
        if (data.chunked) {
            writeIndefiniteLengthType(MajorType.UNICODE_STRING)
            if (data.string == null) {
                return
            }
        } else if (data.string == null) {
            encoder.encode(CborNull)
            return
        }
        val bytes = data.string.encodeToByteArray()
        writeType(MajorType.UNICODE_STRING, bytes.size.toLong())
        writeBytes(*bytes)
    }
}
