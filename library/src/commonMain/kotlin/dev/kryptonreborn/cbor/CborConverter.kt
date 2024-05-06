package dev.kryptonreborn.cbor

import com.ionspin.kotlin.bignum.integer.BigInteger
import dev.kryptonreborn.cbor.decoder.CborHalfPrecisionFloatDecoder
import dev.kryptonreborn.cbor.encoder.CborHalfPrecisionFloatEncoder
import dev.kryptonreborn.cbor.model.*
import kotlinx.io.Buffer
import kotlinx.io.readByteArray

fun Long.toCborElement(): CborElement = if (this >= 0) {
    CborUnsignedInteger(this)
} else {
    CborNegativeInteger(this)
}

fun BigInteger.toCborElement(): CborElement = if (this.signum() == -1) {
    CborNegativeInteger(this)
} else {
    CborUnsignedInteger(this)
}

fun Boolean.toCborElement(): CborElement = if (this) {
    CborTrue
} else {
    CborFalse
}

fun ByteArray?.toCborElement(): CborElement = CborByteString(this)

fun String?.toCborElement(): CborElement = CborUnicodeString(this)

fun Float.toCborElement(): CborElement = if (isHalfPrecisionEnough(this)) {
    CborHalfPrecisionFloat(this)
} else {
    CborSinglePrecisionFloat(this)
}

fun Double.toCborElement(): CborElement = CborDoublePrecisionFloat(this)

fun Long.asCborTag(): CborTag = CborTag(this)

@OptIn(ExperimentalStdlibApi::class)
private fun isHalfPrecisionEnough(value: Float): Boolean {
    return try {
        val bytes = Buffer().use { output ->
            CborHalfPrecisionFloatEncoder(output, CborEncoder(output)).encode(CborHalfPrecisionFloat(value))
            output.readByteArray()
        }

        Buffer().use { input ->
            input.write(bytes)
            val decoder = CborHalfPrecisionFloatDecoder(input, CborDecoder(input))
            if (input.readAtMostTo(ByteArray(1), 0, 1) == -1) {
                throw CborException("unexpected end of stream")
            }
            value == decoder.decode(0).value
        }
    } catch (cborException: CborException) {
        false
    }
}
