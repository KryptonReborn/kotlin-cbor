package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborNegativeInteger
import dev.kryptonreborn.cbor.model.CborUnsignedInteger
import kotlinx.io.Source

class CborNegativeIntegerDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborNegativeInteger>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborNegativeInteger {
        return CborNegativeInteger(MINUS_ONE - getLengthAsBigInteger(initialByte))
    }
}

class CborUnsignedIntegerDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborUnsignedInteger>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborUnsignedInteger {
        return CborUnsignedInteger(getLengthAsBigInteger(initialByte))
    }
}
