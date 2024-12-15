package io.github.kronst.kenv.core.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

@Suppress("UNCHECKED_CAST")
class CollectionValueConverterTest {

    private val converter = CollectionValueConverter(
        converter = CompositeValueConverter(
            converters = listOf(
                StringValueConverter(),
                PrimitiveValueConverter()
            )
        ),
        separator = ",",
    )

    @Test
    fun `converter handles collection of bytes`() {
        val listByte = converter.convert(
            "42, 43, 44",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Byte::class.createType())))
        )
        assertEquals(3, (listByte as List<Byte>).size)
        assertEquals(listOf<Byte>(42, 43, 44), listByte)

        val setByte = converter.convert(
            "42, 43, 44, 42",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Byte::class.createType())))
        )
        assertEquals(3, (setByte as Set<Byte>).size)
        assertEquals(setOf<Byte>(42, 43, 44), setByte)
    }

    @Test
    fun `converter handles collection of shorts`() {
        val listShort = converter.convert(
            "42, 43, 44",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Short::class.createType())))
        )
        assertEquals(3, (listShort as List<Short>).size)
        assertEquals(listOf<Short>(42, 43, 44), listShort)

        val setShort = converter.convert(
            "42, 43, 44, 42",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Short::class.createType())))
        )
        assertEquals(3, (setShort as Set<Short>).size)
        assertEquals(setOf<Short>(42, 43, 44), setShort)
    }

    @Test
    fun `converter handles collection of integers`() {
        val listInt = converter.convert(
            "42, 43, 44",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Int::class.createType())))
        )
        assertEquals(3, (listInt as List<Int>).size)
        assertEquals(listOf(42, 43, 44), listInt)

        val setInt = converter.convert(
            "42, 43, 44, 42",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Int::class.createType())))
        )
        assertEquals(3, (setInt as Set<Int>).size)
        assertEquals(setOf(42, 43, 44), setInt)
    }

    @Test
    fun `converter handles collection of longs`() {
        val listLong = converter.convert(
            "42, 43, 44",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Long::class.createType())))
        )
        assertEquals(3, (listLong as List<Long>).size)
        assertEquals(listOf(42L, 43L, 44L), listLong)

        val setLong = converter.convert(
            "42, 43, 44, 42",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Long::class.createType())))
        )
        assertEquals(3, (setLong as Set<Long>).size)
        assertEquals(setOf(42L, 43L, 44L), setLong)
    }

    @Test
    fun `converter handles collection of floats`() {
        val listFloat = converter.convert(
            "42.0, 43.0, 44.0",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Float::class.createType())))
        )
        assertEquals(3, (listFloat as List<Float>).size)
        assertEquals(listOf(42.0f, 43.0f, 44.0f), listFloat)

        val setFloat = converter.convert(
            "42.0, 43.0, 44.0, 42.0",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Float::class.createType())))
        )
        assertEquals(3, (setFloat as Set<Float>).size)
        assertEquals(setOf(42.0f, 43.0f, 44.0f), setFloat)
    }

    @Test
    fun `converter handles collection of doubles`() {
        val listDouble = converter.convert(
            "42.0, 43.0, 44.0",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Double::class.createType())))
        )
        assertEquals(3, (listDouble as List<Double>).size)
        assertEquals(listOf(42.0, 43.0, 44.0), listDouble)

        val setDouble = converter.convert(
            "42.0, 43.0, 44.0, 42.0",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Double::class.createType())))
        )
        assertEquals(3, (setDouble as Set<Double>).size)
        assertEquals(setOf(42.0, 43.0, 44.0), setDouble)
    }

    @Test
    fun `converter handles collection of booleans`() {
        val listBoolean = converter.convert(
            "true, false, true",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Boolean::class.createType())))
        )
        assertEquals(3, (listBoolean as List<Boolean>).size)
        assertEquals(listOf(true, false, true), listBoolean)

        val setBoolean = converter.convert(
            "true, false, true, true",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Boolean::class.createType())))
        )
        assertEquals(2, (setBoolean as Set<Boolean>).size)
        assertEquals(setOf(true, false), setBoolean)
    }

    @Test
    fun `converter handles collection of chars`() {
        val listChar = converter.convert(
            "a, b, c",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(Char::class.createType())))
        )
        assertEquals(3, (listChar as List<Char>).size)
        assertEquals(listOf('a', 'b', 'c'), listChar)

        val setChar = converter.convert(
            "a, b, c, a",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(Char::class.createType())))
        )
        assertEquals(3, (setChar as Set<Char>).size)
        assertEquals(setOf('a', 'b', 'c'), setChar)
    }

    @Test
    fun `converter handles collection of strings`() {
        val listString = converter.convert(
            "a, b, c",
            List::class.createType(arguments = listOf(KTypeProjection.invariant(String::class.createType())))
        )
        assertEquals(3, (listString as List<String>).size)
        assertEquals(listOf("a", "b", "c"), listString)

        val setString = converter.convert(
            "a, b, c, a",
            Set::class.createType(arguments = listOf(KTypeProjection.invariant(String::class.createType())))
        )
        assertEquals(3, (setString as Set<String>).size)
        assertEquals(setOf("a", "b", "c"), setString)
    }
}
