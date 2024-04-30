package dev.kryptonreborn.cbor

sealed class CborSimpleValue private constructor(
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
}

data object CborFalse : CborSimpleValue(SimpleValueType.FALSE)

data object CborTrue : CborSimpleValue(SimpleValueType.TRUE)

data object CborNull : CborSimpleValue(SimpleValueType.NULL)

data object CborUndefined : CborSimpleValue(SimpleValueType.UNDEFINED)
