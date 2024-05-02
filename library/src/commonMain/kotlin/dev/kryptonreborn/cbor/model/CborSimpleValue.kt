package dev.kryptonreborn.cbor.model

open class CborSimpleValue private constructor(
    val value: Int,
    val simpleValueType: SimpleValueType,
    specialType: SpecialType,
) : CborSpecialElement(specialType) {
    constructor(simpleValueType: SimpleValueType) : this(
        simpleValueType.value,
        simpleValueType,
        SpecialType.SIMPLE_VALUE,
    )

    constructor(value: Int) : this(
        value,
        SimpleValueType.ofByte(value),
        if (value <= 23) SpecialType.SIMPLE_VALUE else SpecialType.SIMPLE_VALUE_NEXT_BYTE,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborSimpleValue) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false
        if (simpleValueType != other.simpleValueType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value
        result = 31 * result + simpleValueType.hashCode()
        return result
    }
}

data object CborFalse : CborSimpleValue(SimpleValueType.FALSE)

data object CborTrue : CborSimpleValue(SimpleValueType.TRUE)

data object CborNull : CborSimpleValue(SimpleValueType.NULL)

data object CborUndefined : CborSimpleValue(SimpleValueType.UNDEFINED)
