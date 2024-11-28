package dev.kronst.kenv.core

import dev.kronst.kenv.config.EmptyValueStrategy
import dev.kronst.kenv.core.converter.ValueConverter
import dev.kronst.kenv.exception.UnsupportedValueTypeException
import kotlin.reflect.KParameter

class ConstructorParameterValueProvider(
    private val converters: List<ValueConverter>,
    private val emptyValueStrategy: EmptyValueStrategy,
) {

    fun provide(value: String?, parameter: KParameter): Any? {
        val type = parameter.type

        if (value.isNullOrBlank()) {
            val result = when (emptyValueStrategy) {
                EmptyValueStrategy.NULL -> null
                EmptyValueStrategy.DEFAULT -> {
                    if (type.isMarkedNullable) {
                        null
                    } else {
                        DefaultValueProvider.provide(type = type)
                    }
                }
            }

            if (result == null && !type.isMarkedNullable) {
                throw IllegalStateException("Cannot provide a null value for non-nullable parameter ${parameter.name}")
            }

            return result
        }

        val converter = converters.find { it.canConvert(type) }
            ?: throw UnsupportedValueTypeException("No value converter found for type $type")

        return converter.convert(value = value, type = type)
    }
}
