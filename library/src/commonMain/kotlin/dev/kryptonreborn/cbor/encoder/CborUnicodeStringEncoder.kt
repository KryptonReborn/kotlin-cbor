package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborNull
import dev.kryptonreborn.cbor.model.CborUnicodeString
import dev.kryptonreborn.cbor.model.MajorType
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
