package io.github.kronst.kenv.core.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.full.createType

class StringValueConverterTest {

    private val converter = StringValueConverter()

    @Test
    fun `converter handles string values`() {
        assertEquals("localhost", converter.convert("localhost", String::class.createType()))
    }
}
