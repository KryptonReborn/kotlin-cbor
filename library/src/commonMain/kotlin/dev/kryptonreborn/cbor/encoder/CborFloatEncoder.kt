package dev.kryptonreborn.cbor.encoder

import dev.kryptonreborn.cbor.model.CborDoublePrecisionFloat
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.model.CborHalfPrecisionFloat
import dev.kryptonreborn.cbor.model.CborSinglePrecisionFloat
import kotlinx.io.Sink

class CborHalfPrecisionFloatEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborHalfPrecisionFloat>(sink, cborEncoder) {
    override fun encode(data: CborHalfPrecisionFloat) {
        val bits = fromFloat(data.value)
        writeBytes(
            ((7 shl 5) or 25).toByte(),
            ((bits shr 8) and 0xFF).toByte(),
            ((bits shr 0) and 0xFF).toByte(),
        )
    }

    /**
     * @param fval the float value
     * @return all higher 16 bits as 0 for all results
     * @see [Stack Overflow](http://stackoverflow.com/a/6162687)
     */
    private fun fromFloat(fval: Float): Int {
        val fbits = fval.toBits()
        val sign = fbits ushr 16 and 0x8000 // sign only
        var roundedValue = 0x1000 + fbits and 0x7fffffff // rounded value
        if (roundedValue >= 0x47800000) // might be or become NaN/Inf
        { // avoid Inf due to rounding
            if ((fbits and 0x7fffffff) >= 0x47800000) { // is or must become
                // NaN/Inf
                if (roundedValue < 0x7f800000) { // was value but too large
                    return sign or 0x7c00 // make it +/-Inf
                }
                return sign or 0x7c00 or ( // remains +/-Inf or NaN
                        (fbits and 0x007fffff) ushr 13) // keep NaN (and
                // Inf) bits
            }
            return sign or 0x7bff // unrounded not quite Inf
        }
        if (roundedValue >= 0x38800000) { // remains normalized value
            return sign or (roundedValue - 0x38000000 ushr 13) // exp - 127 + 15
        }
        if (roundedValue < 0x33000000) { // too small for subnormal
            return sign // becomes +/-0
        }
        roundedValue = (fbits and 0x7fffffff) ushr 23 // tmp exp for subnormal calc
        return sign or (((fbits and 0x7fffff or 0x800000) // add subnormal bit
                + (0x800000 ushr roundedValue - 102) // round depending on cut off
                ) ushr 126 - roundedValue) // div by 2^(1-(exp-127+15)) and >> 13 | exp=0
    }
}

class CborSinglePrecisionFloatEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborSinglePrecisionFloat>(sink, cborEncoder) {
    override fun encode(data: CborSinglePrecisionFloat) {
        val bits = data.value.toRawBits()
        writeBytes(
            ((7 shl 5) or 26).toByte(),
            ((bits shr 24) and 0xFF).toByte(),
            ((bits shr 16) and 0xFF).toByte(),
            ((bits shr 8) and 0xFF).toByte(),
            ((bits shr 0) and 0xFF).toByte(),
        )
    }
}

class CborDoublePrecisionFloatEncoder(
    sink: Sink,
    cborEncoder: CborEncoder,
) : BaseEncoder<CborDoublePrecisionFloat>(sink, cborEncoder) {
    override fun encode(data: CborDoublePrecisionFloat) {
        val bits = data.value.toRawBits()
        writeBytes(
            ((7 shl 5) or 27).toByte(),
            ((bits shr 56) and 0xFFL).toByte(),
            ((bits shr 48) and 0xFFL).toByte(),
            ((bits shr 40) and 0xFFL).toByte(),
            ((bits shr 32) and 0xFFL).toByte(),
            ((bits shr 24) and 0xFFL).toByte(),
            ((bits shr 16) and 0xFFL).toByte(),
            ((bits shr 8) and 0xFFL).toByte(),
            ((bits shr 0) and 0xFFL).toByte(),
        )
    }
}
