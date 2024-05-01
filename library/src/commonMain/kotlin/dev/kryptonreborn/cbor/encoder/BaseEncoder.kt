package dev.kryptonreborn.cbor.encoder

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import dev.kryptonreborn.cbor.AdditionalInformation.*
import dev.kryptonreborn.cbor.CborElement
import dev.kryptonreborn.cbor.CborEncoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.MajorType
import kotlinx.io.Sink


abstract class BaseEncoder<T : CborElement>(
    protected val sink: Sink,
    protected val encoder: CborEncoder,
) {
    companion object {
        val MINUS_ONE = (-1).toBigInteger()
        val UINT64_MAX_PLUS_ONE = "18446744073709551616".toBigInteger()
    }

    @Throws(CborException::class)
    abstract fun encode(data: T)

    @Throws(CborException::class)
    protected fun writeIndefiniteLengthType(majorType: MajorType) {
        var symbol = majorType.value shl 5
        symbol = symbol or INDEFINITE.value
        writeBytes(symbol.toByte())
    }

    @Throws(CborException::class)
    protected fun writeType(majorType: MajorType, length: Long) {
        var symbol = majorType.value shl 5
        when {
            length <= 23L -> {
                writeBytes(
                    (symbol.toLong() or length).toByte()
                )
            }

            length <= 255L -> {
                symbol = symbol or ONE_BYTE.value
                writeBytes(
                    symbol.toByte(),
                    length.toByte(),
                )
            }

            length <= 65535L -> {
                symbol = symbol or TWO_BYTES.value
                writeBytes(
                    symbol.toByte(),
                    (length shr 8).toByte(),
                    (length and 0xFFL).toByte(),
                )
            }

            length <= 4294967295L -> {
                symbol = symbol or FOUR_BYTES.value
                writeBytes(
                    symbol.toByte(),
                    ((length shr 24) and 0xFFL).toByte(),
                    ((length shr 16) and 0xFFL).toByte(),
                    ((length shr 8) and 0xFFL).toByte(),
                    (length and 0xFFL).toByte(),
                )
            }

            else -> {
                symbol = symbol or EIGHT_BYTES.value
                writeBytes(
                    symbol.toByte(),
                    ((length shr 56) and 0xFFL).toByte(),
                    ((length shr 48) and 0xFFL).toByte(),
                    ((length shr 40) and 0xFFL).toByte(),
                    ((length shr 32) and 0xFFL).toByte(),
                    ((length shr 24) and 0xFFL).toByte(),
                    ((length shr 16) and 0xFFL).toByte(),
                    ((length shr 8) and 0xFFL).toByte(),
                    (length and 0xFFL).toByte(),
                )
            }
        }
    }

    @Throws(CborException::class)
    protected fun writeType(majorType: MajorType, length: BigInteger) {
        val negative = majorType === MajorType.NEGATIVE_INTEGER
        var symbol = majorType.value shl 5
        when {
            length < 24.toBigInteger() -> {
                writeBytes(
                    (symbol or length.intValue()).toByte(),
                )
            }

            length < 256.toBigInteger() -> {
                symbol = symbol or ONE_BYTE.value
                writeBytes(
                    symbol.toByte(),
                    length.intValue().toByte(),
                )
            }

            length < 65536L.toBigInteger() -> {
                symbol = symbol or TWO_BYTES.value
                val twoByteValue = length.longValue()
                writeBytes(
                    symbol.toByte(),
                    (twoByteValue shr 8).toByte(),
                    (twoByteValue and 0xFFL).toByte(),
                )
            }

            length < 4294967296L.toBigInteger() -> {
                symbol = symbol or FOUR_BYTES.value
                val fourByteValue = length.longValue()
                writeBytes(
                    symbol.toByte(),
                    ((fourByteValue shr 24) and 0xFFL).toByte(),
                    ((fourByteValue shr 16) and 0xFFL).toByte(),
                    ((fourByteValue shr 8) and 0xFFL).toByte(),
                    (fourByteValue and 0xFFL).toByte()
                )
            }

            length < UINT64_MAX_PLUS_ONE -> {
                symbol = symbol or EIGHT_BYTES.value
                val mask = (0xFF).toBigInteger()
                writeBytes(
                    symbol.toByte(),
                    length.shr(56).and(mask).byteValue(),
                    length.shr(48).and(mask).byteValue(),
                    length.shr(40).and(mask).byteValue(),
                    length.shr(32).and(mask).byteValue(),
                    length.shr(24).and(mask).byteValue(),
                    length.shr(16).and(mask).byteValue(),
                    length.shr(8).and(mask).byteValue(),
                    length.and(mask).byteValue()
                )
            }

            else -> {
                if (negative) {
                    writeType(MajorType.TAG, 3)
                } else {
                    writeType(MajorType.TAG, 2)
                }
                val bytes = length.toByteArray()
                writeType(MajorType.BYTE_STRING, bytes.size.toLong())
                writeBytes(*bytes)
            }
        }
    }

    @Throws(CborException::class)
    protected fun writeBytes(vararg bytes: Byte) {
        try {
            sink.write(bytes)
        } catch (e: IndexOutOfBoundsException) {
            throw CborException(
                "Error writing to output, startIndex or endIndex is out of range of source array indices.", e
            )
        } catch (e: IllegalArgumentException) {
            throw CborException("Error writing to output, startIndex > endInde.", e)
        } catch (e: IllegalStateException) {
            throw CborException("Error writing to output, the sink is closed.", e)
        }
    }
}
