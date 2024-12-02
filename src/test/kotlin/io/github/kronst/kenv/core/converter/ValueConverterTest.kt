package io.github.kronst.kenv.core.converter

import io.github.kronst.kenv.exception.MissingConverterImplementationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.createType

class ValueConverterTest {

    @Test
    fun `string converter handles string values`() {
        val converter = StringValueConverter()

        assertEquals("localhost", converter.convert("localhost", String::class.createType()))
    }

    @Test
    fun `primitive converter handles all basic values`() {
        val converter = PrimitiveValueConverter()

        assertEquals(42.toByte(), converter.convert("42", Byte::class.createType()))
        assertEquals(42.toShort(), converter.convert("42", Short::class.createType()))
        assertEquals(42, converter.convert("42", Int::class.createType()))
        assertEquals(42L, converter.convert("42", Long::class.createType()))
        assertEquals(42.0f, converter.convert("42", Float::class.createType()))
        assertEquals(42.0, converter.convert("42", Double::class.createType()))
        assertEquals(false, converter.convert("false", Boolean::class.createType()))
        assertEquals('\u0000', converter.convert("\u0000", Char::class.createType()))
    }

    @Test
    fun `when unknown type then primitive converter throws exception`() {
        val converter = PrimitiveValueConverter()

        assertThrows<MissingConverterImplementationException> { converter.convert("42", String::class.createType()) }
    }
}
