package io.github.kronst.kenv.core.converter

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType

class MapValueConverterTest {

    private val converter = MapValueConverter(
        converters = listOf(
            PrimitiveValueConverter.instance,
            StringValueConverter.instance,
            CollectionValueConverter(
                listOf(
                    StringValueConverter.instance,
                    PrimitiveValueConverter.instance,
                ),
                separator = ","
            )
        ),
        itemSeparator = ";",
        keyValueSeparator = "=",
    )

    @Test
    fun `converter handles map of string-int pairs`() {
        val result = converter.convert(
            "key1=42; key2=43; key3=44",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(String::class.createType()),
                    KTypeProjection.invariant(Int::class.createType())
                )
            )
        )

        assertEquals(
            mapOf(
                "key1" to 42,
                "key2" to 43,
                "key3" to 44
            ),
            result
        )
    }

    @Test
    fun `converter handles map of string-string pairs`() {
        val result = converter.convert(
            "key1=value1; key2=value2; key3=value3",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(String::class.createType()),
                    KTypeProjection.invariant(String::class.createType())
                )
            )
        )

        assertEquals(
            mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            ),
            result
        )
    }

    @Test
    fun `converter handles map of string-list pairs with empty values`() {
        val result = converter.convert(
            "key1=; key2=a,b,c; key3=,d,e,f",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(String::class.createType()),
                    KTypeProjection.invariant(
                        List::class.createType(
                            arguments = listOf(
                                KTypeProjection.invariant(String::class.createType())
                            )
                        )
                    )
                )
            )
        )

        assertEquals(
            mapOf(
                "key1" to emptyList(),
                "key2" to listOf("a", "b", "c"),
                "key3" to listOf("d", "e", "f")
            ),
            result
        )
    }

    @Test
    fun `converter handles map of string-booleans pairs`() {
        val result = converter.convert(
            "key1=true; key2=false; key3=true",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(String::class.createType()),
                    KTypeProjection.invariant(Boolean::class.createType())
                )
            )
        )

        assertEquals(
            mapOf(
                "key1" to true,
                "key2" to false,
                "key3" to true
            ),
            result
        )
    }

    @Test
    fun `converter handles map of string-list pairs`() {
        val result = converter.convert(
            "key1=a,b,c; key2=d,e,f; key3=g,h,i",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(String::class.createType()),
                    KTypeProjection.invariant(
                        List::class.createType(
                            arguments = listOf(
                                KTypeProjection.invariant(String::class.createType())
                            )
                        )
                    )
                )
            )
        )

        assertEquals(
            mapOf(
                "key1" to listOf("a", "b", "c"),
                "key2" to listOf("d", "e", "f"),
                "key3" to listOf("g", "h", "i")
            ),
            result
        )
    }

    @Test
    fun `converter handles map of int-set pairs`() {
        val result = converter.convert(
            "1=1,2,3,3; 2=4,5,5,6; 3=7,7,8,9",
            Map::class.createType(
                arguments = listOf(
                    KTypeProjection.invariant(Int::class.createType()),
                    KTypeProjection.invariant(
                        Set::class.createType(
                            arguments = listOf(
                                KTypeProjection.invariant(Int::class.createType())
                            )
                        )
                    )
                )
            )
        )

        assertEquals(
            mapOf(
                1 to setOf(1, 2, 3),
                2 to setOf(4, 5, 6),
                3 to setOf(7, 8, 9)
            ),
            result
        )
    }
}
