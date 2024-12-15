package io.github.kronst.kenv.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.test.assertTrue

class DefaultValueProviderTest {

    @Test
    fun `when unsupported type then return null`() {
        val value = DefaultValueProvider.provide(Any::class.createType())

        assertNull(value)
    }

    @Test
    fun `when type Byte then return 0`() {
        val value = DefaultValueProvider.provide(Byte::class.createType())

        assertTrue(value is Byte)
        assertEquals(0.toByte(), value)
    }

    @Test
    fun `when type Short then return 0`() {
        val value = DefaultValueProvider.provide(Short::class.createType())

        assertTrue(value is Short)
        assertEquals(0.toShort(), value)
    }

    @Test
    fun `when type Int then return 0`() {
        val value = DefaultValueProvider.provide(Int::class.createType())

        assertTrue(value is Int)
        assertEquals(0, value)
    }

    @Test
    fun `when type Long then return 0`() {
        val value = DefaultValueProvider.provide(Long::class.createType())

        assertTrue(value is Long)
        assertEquals(0L, value)
    }

    @Test
    fun `when type Float then return 0`() {
        val value = DefaultValueProvider.provide(Float::class.createType())

        assertTrue(value is Float)
        assertEquals(0.0f, value)
    }

    @Test
    fun `when type Double then return 0`() {
        val value = DefaultValueProvider.provide(Double::class.createType())

        assertTrue(value is Double)
        assertEquals(0.0, value)
    }

    @Test
    fun `when type Boolean then return false`() {
        val value = DefaultValueProvider.provide(Boolean::class.createType())

        assertTrue(value is Boolean)
        assertEquals(false, value)
    }

    @Test
    fun `when type Char then return null character`() {
        val value = DefaultValueProvider.provide(Char::class.createType())

        assertTrue(value is Char)
        assertEquals('\u0000', value)
    }

    @Test
    fun `when type String then return empty string`() {
        val value = DefaultValueProvider.provide(String::class.createType())

        assertTrue(value is String)
        assertEquals("", value)
    }

    @Test
    fun `when type List then return empty list`() {
        val value = DefaultValueProvider.provide(List::class.createType(arguments = listOf(KTypeProjection.STAR)))

        assertTrue(value is List<*>)
        assertEquals(emptyList<Nothing>(), value)
    }

    @Test
    fun `when type Set then return empty set`() {
        val value = DefaultValueProvider.provide(Set::class.createType(arguments = listOf(KTypeProjection.STAR)))

        assertTrue(value is Set<*>)
        assertEquals(emptySet<Nothing>(), value)
    }

    @Test
    fun `when type Map then return empty map`() {
        val value = DefaultValueProvider.provide(
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.STAR,
                    KTypeProjection.STAR
                )
            )
        )

        assertTrue(value is Map<*, *>)
        assertEquals(emptyMap<Nothing, Nothing>(), value)
    }
}
