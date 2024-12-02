package io.github.kronst.kenv.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

class DefaultValueProviderTest {

    @Test
    fun `when unsupported type then return null`() {
        val value = DefaultValueProvider.provide(Any::class.createType())

        assertNull(value)
    }

    @Test
    fun `when type Byte then return 0`() {
        val value = DefaultValueProvider.provide(Byte::class.createType())

        assertInstanceOf(Byte::class.javaObjectType, value)
        assertEquals(0.toByte(), value)
    }

    @Test
    fun `when type Short then return 0`() {
        val value = DefaultValueProvider.provide(Short::class.createType())

        assertInstanceOf(Short::class.javaObjectType, value)
        assertEquals(0.toShort(), value)
    }

    @Test
    fun `when type Int then return 0`() {
        val value = DefaultValueProvider.provide(Int::class.createType())

        assertInstanceOf(Int::class.javaObjectType, value)
        assertEquals(0, value)
    }

    @Test
    fun `when type Long then return 0`() {
        val value = DefaultValueProvider.provide(Long::class.createType())

        assertInstanceOf(Long::class.javaObjectType, value)
        assertEquals(0L, value)
    }

    @Test
    fun `when type Float then return 0`() {
        val value = DefaultValueProvider.provide(Float::class.createType())

        assertInstanceOf(Float::class.javaObjectType, value)
        assertEquals(0.0f, value)
    }

    @Test
    fun `when type Double then return 0`() {
        val value = DefaultValueProvider.provide(Double::class.createType())

        assertInstanceOf(Double::class.javaObjectType, value)
        assertEquals(0.0, value)
    }

    @Test
    fun `when type Boolean then return false`() {
        val value = DefaultValueProvider.provide(Boolean::class.createType())

        assertInstanceOf(Boolean::class.javaObjectType, value)
        assertEquals(false, value)
    }

    @Test
    fun `when type Char then return null character`() {
        val value = DefaultValueProvider.provide(Char::class.createType())

        assertInstanceOf(Char::class.javaObjectType, value)
        assertEquals('\u0000', value)
    }

    @Test
    fun `when type String then return empty string`() {
        val value = DefaultValueProvider.provide(String::class.createType())

        assertInstanceOf(String::class.java, value)
        assertEquals("", value)
    }

    @Test
    fun `when type List then return empty list`() {
        val value = DefaultValueProvider.provide(List::class.createType(arguments = listOf(KTypeProjection.STAR)))

        assertInstanceOf(List::class.java, value)
        assertEquals(emptyList<Nothing>(), value)
    }

    @Test
    fun `when type Set then return empty set`() {
        val value = DefaultValueProvider.provide(Set::class.createType(arguments = listOf(KTypeProjection.STAR)))

        assertInstanceOf(Set::class.java, value)
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

        assertInstanceOf(Map::class.java, value)
        assertEquals(emptyMap<Nothing, Nothing>(), value)
    }
}
