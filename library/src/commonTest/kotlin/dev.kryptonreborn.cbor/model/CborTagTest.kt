package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

class CborTagTest {
    @Test
    fun testEquals1() {
        for (i in 0..255) {
            val cborTag1 = CborTag(i.toLong())
            val cborTag2 = CborTag(i.toLong())
            assertEquals(cborTag1, cborTag2)
        }
    }

    @Test
    fun testEquals2() {
        val cborTag = CborTag(0)
        assertEquals(cborTag, cborTag)
    }

    @Test
    fun testNotEquals() {
        val cborTag = CborTag(0)
        assertNotEquals(cborTag, CborTag(1))
        assertFalse(cborTag.equals(null))
        assertFalse(cborTag.equals(false))
        assertFalse(cborTag.equals(""))
        assertFalse(cborTag.equals(1))
        assertFalse(cborTag.equals(1.1))
    }
}
