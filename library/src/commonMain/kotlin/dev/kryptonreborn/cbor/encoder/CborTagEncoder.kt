package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborTag
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink

class CborTagEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborTag>(sink, cborEncoder) {
    override fun encode(data: CborTag) {
        writeType(MajorType.TAG, data.value)
    }
}
