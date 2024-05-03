package dev.kryptonreborn.cbor.model

import dev.kryptonreborn.cbor.CborException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SpecialTypeTest {
    @Test
    fun shouldDetectUnallocated28() {
        assertFailsWith<CborException> { SpecialType.ofByte(28) }
    }

    @Test
    fun shouldDetectUnallocated29() {
        assertFailsWith<CborException> { SpecialType.ofByte(29) }
    }

    @Test
    fun shouldDetectUnallocated30() {
        assertFailsWith<CborException> { SpecialType.ofByte(30) }
    }
}
