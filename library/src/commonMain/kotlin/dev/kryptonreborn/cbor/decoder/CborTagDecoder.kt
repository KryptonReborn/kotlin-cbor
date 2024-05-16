package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborTag
import kotlinx.io.Source

class CborTagDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborTag>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborTag {
        return CborTag(getLength(initialByte))
    }
}
