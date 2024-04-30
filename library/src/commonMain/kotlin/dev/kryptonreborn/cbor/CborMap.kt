package dev.kryptonreborn.cbor

open class CborMap(
    private val data: MutableMap<CborElement, CborElement>,
) : CborChunkableElement(majorType = MajorType.MAP) {
    fun keys(): Set<CborElement> = data.keys.toSet()

    fun values(): List<CborElement> = data.values.toList()

    fun put(key: CborElement, value: CborElement): CborElement? = data.put(key, value)

    fun get(key: CborElement): CborElement? = data[key]

    fun remove(key: CborElement): CborElement? = data.remove(key)
}

