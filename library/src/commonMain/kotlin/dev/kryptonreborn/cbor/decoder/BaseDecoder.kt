package dev.kryptonreborn.cbor.decoder

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import dev.kryptonreborn.cbor.CborDecoder
import dev.kryptonreborn.cbor.CborException
import dev.kryptonreborn.cbor.model.AdditionalInformation
import dev.kryptonreborn.cbor.model.CborElement
import kotlinx.io.Buffer
import kotlinx.io.EOFException
import kotlinx.io.Source
import kotlinx.io.readByteArray
import kotlinx.io.readUByte
import kotlin.math.min

abstract class BaseDecoder<T : CborElement>(
    protected val input: Source,
    protected val decoder: CborDecoder,
) {
    companion object {
        val MINUS_ONE = (-1).toBigInteger()
        const val BUFFER_SIZE = 4096
        const val INFINITY = -1L
    }

    @Throws(CborException::class)
    abstract fun decode(initialByte: Int): T

    @Throws(CborException::class)
    protected fun nextSymbol(): Int {
        try {
            return input.readUByte().toInt()
        } catch (e: EOFException) {
            throw CborException("Error reading from input, there are no more bytes to read.", e)
        } catch (e: IllegalArgumentException) {
            throw CborException("Error reading from input, the source is closed", e)
        }
    }

    @Throws(CborException::class)
    protected fun nextSymbols(amount: Int): ByteArray {
        try {
            return input.readByteArray(amount)
        } catch (eofException: EOFException) {
            throw CborException(
                "Error reading from input, the underlying source is exhausted before byteCount bytes of data could be read.",
                eofException,
            )
        } catch (e: IllegalArgumentException) {
            throw CborException("Error reading from input, byteCount is negative", e)
        } catch (e: IllegalStateException) {
            throw CborException("Error reading from input, the source is closed", e)
        }
    }

    @Throws(CborException::class)
    protected fun decodeBytes(length: Long): ByteArray {
        if (length > Int.MAX_VALUE) {
            throw CborException("Decoding fixed size items is limited to INTMAX")
        }
        val bytes = Buffer()
        val chunkSize = min(BUFFER_SIZE.toLong(), length).toInt()
        var i = length
        val buf = ByteArray(chunkSize)
        while (i > 0) {
            try {
                val read = input.readAtMostTo(buf, 0, min(chunkSize.toLong(), i).toInt())
                if (read == -1) {
                    throw EOFException("Unexpected end of stream")
                }
                bytes.write(buf, 0, read)
                i -= read
            } catch (e: EOFException) {
                throw CborException("Error reading from input, the source is exhausted", e)
            } catch (e: IndexOutOfBoundsException) {
                throw CborException(
                    "Error reading from input, startIndex or endIndex is out of range of sink array indices.",
                    e,
                )
            } catch (e: IllegalStateException) {
                throw CborException("Error reading from input, the source is closed", e)
            } catch (e: IllegalArgumentException) {
                throw CborException("Error reading from input, startIndex > endIndex", e)
            }
        }
        return bytes.readByteArray()
    }

    @Throws(CborException::class)
    protected fun getLength(initialByte: Int): Long {
        when (AdditionalInformation.ofByte(initialByte)) {
            AdditionalInformation.DIRECT -> return (initialByte and 31).toLong()
            AdditionalInformation.ONE_BYTE -> return nextSymbol().toLong()
            AdditionalInformation.TWO_BYTES -> {
                var twoByteValue = 0L
                val symbols = nextSymbols(2)
                twoByteValue = twoByteValue or ((symbols[0].toInt() and 0xFF) shl 8).toLong()
                twoByteValue = twoByteValue or ((symbols[1].toInt() and 0xFF) shl 0).toLong()
                return twoByteValue
            }

            AdditionalInformation.FOUR_BYTES -> {
                var fourByteValue = 0L
                val symbols = nextSymbols(4)
                fourByteValue = fourByteValue or ((symbols[0].toInt() and 0xFF).toLong() shl 24)
                fourByteValue = fourByteValue or ((symbols[1].toInt() and 0xFF).toLong() shl 16)
                fourByteValue = fourByteValue or ((symbols[2].toInt() and 0xFF).toLong() shl 8)
                fourByteValue = fourByteValue or ((symbols[3].toInt() and 0xFF).toLong() shl 0)
                return fourByteValue
            }

            AdditionalInformation.EIGHT_BYTES -> {
                var eightByteValue = 0L
                val symbols = nextSymbols(8)
                eightByteValue = eightByteValue or ((symbols[0].toInt() and 0xFF).toLong() shl 56)
                eightByteValue = eightByteValue or ((symbols[1].toInt() and 0xFF).toLong() shl 48)
                eightByteValue = eightByteValue or ((symbols[2].toInt() and 0xFF).toLong() shl 40)
                eightByteValue = eightByteValue or ((symbols[3].toInt() and 0xFF).toLong() shl 32)
                eightByteValue = eightByteValue or ((symbols[4].toInt() and 0xFF).toLong() shl 24)
                eightByteValue = eightByteValue or ((symbols[5].toInt() and 0xFF).toLong() shl 16)
                eightByteValue = eightByteValue or ((symbols[6].toInt() and 0xFF).toLong() shl 8)
                eightByteValue = eightByteValue or ((symbols[7].toInt() and 0xFF).toLong() shl 0)
                return eightByteValue
            }

            AdditionalInformation.INDEFINITE -> return INFINITY
            AdditionalInformation.RESERVED -> throw CborException("Reserved additional information")
            else -> throw CborException("Reserved additional information")
        }
    }

    @Throws(CborException::class)
    protected fun getLengthAsBigInteger(initialByte: Int): BigInteger {
        when (AdditionalInformation.ofByte(initialByte)) {
            AdditionalInformation.DIRECT -> return (initialByte and 31).toLong().toBigInteger()
            AdditionalInformation.ONE_BYTE -> return nextSymbol().toLong().toBigInteger()
            AdditionalInformation.TWO_BYTES -> {
                var twoByteValue = 0L
                val symbols = nextSymbols(2)
                twoByteValue = twoByteValue or ((symbols[0].toInt() and 0xFF) shl 8).toLong()
                twoByteValue = twoByteValue or ((symbols[1].toInt() and 0xFF) shl 0).toLong()
                return twoByteValue.toBigInteger()
            }

            AdditionalInformation.FOUR_BYTES -> {
                var fourByteValue = 0L
                val symbols = nextSymbols(4)
                fourByteValue = fourByteValue or ((symbols[0].toInt() and 0xFF).toLong() shl 24)
                fourByteValue = fourByteValue or ((symbols[1].toInt() and 0xFF).toLong() shl 16)
                fourByteValue = fourByteValue or ((symbols[2].toInt() and 0xFF).toLong() shl 8)
                fourByteValue = fourByteValue or ((symbols[3].toInt() and 0xFF).toLong() shl 0)
                return fourByteValue.toBigInteger()
            }

            AdditionalInformation.EIGHT_BYTES -> {
                var eightByteValue = BigInteger.ZERO
                val symbols = nextSymbols(8)
                eightByteValue = eightByteValue or ((symbols[0].toInt() and 0xFF).toLong().toBigInteger() shl 56)
                eightByteValue = eightByteValue or ((symbols[1].toInt() and 0xFF).toLong().toBigInteger() shl 48)
                eightByteValue = eightByteValue or ((symbols[2].toInt() and 0xFF).toLong().toBigInteger() shl 40)
                eightByteValue = eightByteValue or ((symbols[3].toInt() and 0xFF).toLong().toBigInteger() shl 32)
                eightByteValue = eightByteValue or ((symbols[4].toInt() and 0xFF).toLong().toBigInteger() shl 24)
                eightByteValue = eightByteValue or ((symbols[5].toInt() and 0xFF).toLong().toBigInteger() shl 16)
                eightByteValue = eightByteValue or ((symbols[6].toInt() and 0xFF).toLong().toBigInteger() shl 8)
                eightByteValue = eightByteValue or ((symbols[7].toInt() and 0xFF).toLong().toBigInteger() shl 0)
                return eightByteValue
            }

            AdditionalInformation.INDEFINITE -> return INFINITY.toBigInteger()
            AdditionalInformation.RESERVED -> throw CborException("Reserved additional information")
            else -> throw CborException("Reserved additional information")
        }
    }
}
