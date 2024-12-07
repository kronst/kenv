package io.github.kronst.kenv.core

import io.github.kronst.kenv.config.EmptyValueStrategy
import io.github.kronst.kenv.core.converter.CompositeValueConverter
import kotlin.reflect.KParameter

class ConstructorParameterValueProvider(
    private val converter: CompositeValueConverter,
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

        return converter.convert(value = value, type = type)
    }
}
