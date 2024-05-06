package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals

class CborLanguageTaggedStringTest {
    @Test
    fun shouldInitializeWithStrings() {
        val lts = CborLanguageTaggedString("en", "Hello")
        assertEquals(38, lts.tag!!.value)
        assertEquals("en", lts.language.string)
        assertEquals("Hello", lts.string.string)
    }
}
