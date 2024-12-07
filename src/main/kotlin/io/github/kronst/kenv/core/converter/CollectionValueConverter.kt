package io.github.kronst.kenv.core.converter

import io.github.kronst.kenv.exception.MissingConverterImplementationException
import io.github.kronst.kenv.exception.UnsupportedValueTypeException
import kotlin.reflect.KType

class CollectionValueConverter(
    private val converters: List<ValueConverter>,
    private val separator: String,
) : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return type.classifier in supportedTypes
    }

    override fun convert(value: String, type: KType): Any {
        val elementType = type.arguments.firstOrNull()?.type
            ?: throw IllegalArgumentException("Cannot determine element type for collection $type")

        val elementConverter = converters.find { it.canConvert(elementType) }
            ?: throw UnsupportedValueTypeException("No value converter found for type $elementType")

        val rawElements = value.split(separator)
            .asSequence()
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toList()

        val elements = rawElements.map { element -> elementConverter.convert(value = element, type = elementType) }

        return when (type.classifier) {
            List::class -> elements.toList()
            Set::class -> elements.toSet()
            else -> throw MissingConverterImplementationException("No convert operation for type $type")
        }
    }

    companion object {
        private val supportedTypes = setOf(
            List::class,
            Set::class,
        )
    }
}
