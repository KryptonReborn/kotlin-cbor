package dev.kryptonreborn.cbor.decoder

import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.CborDoublePrecisionFloat
import dev.kryptonreborn.cbor.model.CborHalfPrecisionFloat
import dev.kryptonreborn.cbor.model.CborSinglePrecisionFloat
import kotlinx.io.Source
import kotlin.math.pow

class CborHalfPrecisionFloatDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborHalfPrecisionFloat>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborHalfPrecisionFloat {
        val symbols = nextSymbols(2)
        val bits = (symbols[0].toInt() and 0xFF) shl 8 or (symbols[1].toInt() and 0xFF)
        return CborHalfPrecisionFloat(toFloat(bits))
    }

    /**
     * @see http://stackoverflow.com/a/5684578
     */
    private fun toFloat(bits: Int): Float {
        val s = (bits and 0x8000) shr 15
        val e = (bits and 0x7C00) shr 10
        val f = bits and 0x03FF

        if (e == 0) {
            return ((if (s != 0) -1 else 1) * 2f.pow(-14.0f) * (f / 2f.pow(10.0f)))
        } else if (e == 0x1F) {
            return if (f != 0) Float.NaN else (if (s != 0) -1 else 1) * Float.POSITIVE_INFINITY
        }

        return ((if (s != 0) -1 else 1) * 2f.pow((e - 15)) * (1 + f / 2f.pow(10.0f)))
    }
}

class CborSinglePrecisionFloatDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborSinglePrecisionFloat>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborSinglePrecisionFloat {
        var bits = 0
        val symbols = nextSymbols(4)
        bits = bits or (symbols[0].toInt() and 0xFF)
        bits = bits shl 8
        bits = bits or (symbols[1].toInt() and 0xFF)
        bits = bits shl 8
        bits = bits or (symbols[2].toInt() and 0xFF)
        bits = bits shl 8
        bits = bits or (symbols[3].toInt() and 0xFF)
        return CborSinglePrecisionFloat(Float.fromBits(bits))
    }
}

class CborDoublePrecisionFloatDecoder(
    input: Source,
    decoder: CborDecoder,
) : BaseDecoder<CborDoublePrecisionFloat>(input, decoder) {
    @Throws(CborException::class)
    override fun decode(initialByte: Int): CborDoublePrecisionFloat {
        var bits: Long = 0
        val symbols = nextSymbols(8)
        bits = bits or (symbols[0].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[1].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[2].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[3].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[4].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[5].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[6].toInt() and 0xFF).toLong()
        bits = bits shl 8
        bits = bits or (symbols[7].toInt() and 0xFF).toLong()
        return CborDoublePrecisionFloat(Double.fromBits(bits))
    }
}
