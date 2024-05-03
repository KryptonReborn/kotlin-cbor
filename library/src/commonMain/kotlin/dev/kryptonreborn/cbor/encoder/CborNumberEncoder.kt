package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborNegativeInteger
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import dev.kryptonreborn.cbor.model.MajorType
import kotlinx.io.Sink

class CborNegativeIntegerEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborNegativeInteger>(sink, cborEncoder) {
    override fun encode(data: CborNegativeInteger) {
        writeType(MajorType.NEGATIVE_INTEGER, (MINUS_ONE - data.value).abs())
    }
}

class CborUnsignedIntegerEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborUnsignedInteger>(sink, cborEncoder) {
    override fun encode(data: CborUnsignedInteger) {
        writeType(MajorType.UNSIGNED_INTEGER, data.value)
    }
}
