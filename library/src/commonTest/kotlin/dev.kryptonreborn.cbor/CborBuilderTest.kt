package dev.kryptonreborn.cbor

import dev.kryptonreborn.cbor.model.CborElement
import dev.kryptonreborn.cbor.model.CborTag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CborBuilderTest {
    @Test
    fun shouldResetCborElements() {
        val builder = CborBuilder()
        builder.add(true)
        builder.add(1.0f)
        assertEquals(2, builder.build().size)
        builder.reset()
        assertEquals(0, builder.build().size)
    }

    @Test
    fun shouldAddTag() {
        val builder = CborBuilder()
        val cborElements: List<CborElement> = builder.addTag(1234).build()
        assertEquals(1, cborElements.size.toLong())
        assertTrue(cborElements[0] is CborTag)
        assertEquals(1234, (cborElements[0] as CborTag).value)
    }
}
