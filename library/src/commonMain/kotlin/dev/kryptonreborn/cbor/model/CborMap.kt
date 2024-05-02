package dev.kryptonreborn.cbor.model

open class CborMap(
    private val data: MutableMap<CborElement, CborElement> = mutableMapOf(),
) : CborChunkableElement(majorType = MajorType.MAP) {
    fun keys(): Set<CborElement> = data.keys.toSet()

    fun values(): List<CborElement> = data.values.toList()

    fun put(key: CborElement, value: CborElement): CborElement? = data.put(key, value)

    fun get(key: CborElement): CborElement? = data[key]

    fun remove(key: CborElement): CborElement? = data.remove(key)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborMap) return false
        if (!super.equals(other)) return false

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + data.hashCode()
        return result
    }
}

