package io.github.kronst.kenv.core.converter

import io.github.kronst.kenv.exception.UnsupportedValueTypeException
import kotlin.reflect.KType

class MapValueConverter(
    private val converters: List<ValueConverter>,
    private val itemSeparator: String,
    private val keyValueSeparator: String,
) : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return type.classifier in supportedTypes
    }

    override fun convert(value: String, type: KType): Any {
        val keyType = type.arguments.getOrNull(0)?.type
            ?: throw IllegalArgumentException("Cannot determine key type for map $type")

        val valueType = type.arguments.getOrNull(1)?.type
            ?: throw IllegalArgumentException("Cannot determine value type for map $type")

        val keyConverter = converters.find { it.canConvert(keyType) }
            ?: throw UnsupportedValueTypeException("No value converter found for key type $keyType")

        val valueConverter = converters.find { it.canConvert(valueType) }
            ?: throw UnsupportedValueTypeException("No value converter found for value type $valueType")

        return value.split(itemSeparator)
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .map { entry ->
                val (rawKey, rawValue) = entry.split(keyValueSeparator, limit = 2)
                    .map { it.trim() }
                    .takeIf { it.size == 2 }
                    ?: throw IllegalArgumentException("Invalid format: $entry. Expected: key$keyValueSeparator$value")

                val k = keyConverter.convert(value = rawKey, type = keyType)
                val v = valueConverter.convert(value = rawValue, type = valueType)
                k to v
            }
            .toMap()
    }

    companion object {
        private val supportedTypes = setOf(
            Map::class,
        )
    }
}
