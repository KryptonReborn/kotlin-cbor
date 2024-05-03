package dev.kryptonreborn.cbor.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AdditionalInformationTest {
    /**
     * Additional information values 28 to 30 are reserved for future expansion.
     */
    @Test
    fun shouldHandleReserved28() {
        assertEquals(AdditionalInformation.RESERVED, AdditionalInformation.ofByte(28))
        assertEquals(AdditionalInformation.RESERVED, AdditionalInformation.ofByte(29))
        assertEquals(AdditionalInformation.RESERVED, AdditionalInformation.ofByte(30))
    }
}
