package dev.kryptonreborn.cbor.decoder

import com.ionspin.kotlin.bignum.integer.toBigInteger
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.CborNegativeInteger
import dev.kryptonreborn.cbor.CborUnsignedInteger
import kotlinx.io.Source

class CborNegativeIntegerDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborNegativeInteger>(input, decoder) {
    companion object {
        val MINUS_ONE = (-1).toBigInteger()
    }

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
