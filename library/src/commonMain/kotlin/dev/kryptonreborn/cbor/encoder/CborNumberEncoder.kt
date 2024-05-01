package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborNegativeInteger
import dev.kryptonreborn.cbor.CborUnsignedInteger
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink

class CborNegativeIntegerEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborNegativeInteger>(sink, cborEncoder) {
    override fun encode(data: CborNegativeInteger) {
        writeType(MajorType.NEGATIVE_INTEGER, MINUS_ONE - data.value.abs())
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
