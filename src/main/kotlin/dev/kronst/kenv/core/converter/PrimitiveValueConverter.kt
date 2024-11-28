package dev.kronst.kenv.core.converter

import dev.kronst.kenv.exception.MissingConverterImplementationException
import kotlin.reflect.KType

class PrimitiveValueConverter : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return type.classifier in supportedTypes
    }

    override fun convert(value: String, type: KType): Any? {
        return when (type.classifier) {
            Byte::class -> value.toByteOrNull()
            Short::class -> value.toShortOrNull()
            Int::class -> value.toIntOrNull()
            Long::class -> value.toLongOrNull()
            Float::class -> value.toFloatOrNull()
            Double::class -> value.toDoubleOrNull()
            Boolean::class -> value.toBooleanStrictOrNull()
            Char::class -> value.singleOrNull()
            else -> throw MissingConverterImplementationException("No convert operation for type $type")
        }
    }

    companion object {
        private val supportedTypes = setOf(
            Byte::class,
            Short::class,
            Int::class,
            Long::class,
            Float::class,
            Double::class,
            Boolean::class,
            Char::class,
        )
    }
}
