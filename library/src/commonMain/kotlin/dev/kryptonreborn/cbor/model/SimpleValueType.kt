package dev.kryptonreborn.cbor.model

enum class SimpleValueType(val value: Int) {
    FALSE(20),
    TRUE(21),
    NULL(22),
    UNDEFINED(23),
    RESERVED(0),
    UNALLOCATED(0),
    ;

    companion object {
        fun ofByte(b: Int): SimpleValueType =
            when (b and 31) {
                20 -> FALSE
                21 -> TRUE
                22 -> NULL
                23 -> UNDEFINED
                24, 25, 26, 27, 28, 29, 30, 31 -> RESERVED
                else -> UNALLOCATED
            }
    }
}
