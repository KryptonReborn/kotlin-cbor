package dev.kryptonreborn.cbor.model

data class CborTag(
    val value: Long,
) : CborElement(MajorType.TAG) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CborTag) return false
        if (!super.equals(other)) return false

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}
