package io.github.kronst.kenv.core

import io.github.kronst.kenv.annotation.EnvConfiguration
import io.github.kronst.kenv.annotation.EnvProperty
import io.github.kronst.kenv.config.KenvConfig
import io.github.kronst.kenv.exception.MissingRequiredPropertyException
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

object KenvLoader {
    private val reader = EnvFileReader()

    /**
     * Loads environment variables into a target class.
     * @param target The target class to load
     * @param config The configuration to use when loading the environment variables
     * @return The loaded configuration class
     *
     * @throws IllegalArgumentException If the class is not annotated with [EnvConfiguration]
     * @throws IllegalArgumentException If the class does not have a primary constructor
     * @throws MissingRequiredPropertyException If a required property is not set
     */
    fun <T : Any> load(target: KClass<T>, config: KenvConfig): T {
        if (!target.java.isAnnotationPresent(EnvConfiguration::class.java)) {
            throw IllegalArgumentException("Class ${target.qualifiedName} must be annotated with @EnvConfiguration")
        }

        val constructor = target.primaryConstructor
            ?: throw IllegalArgumentException("Class ${target.qualifiedName} must have a primary constructor")

        val envValues = reader.read(path = config.getFullEnvFilePath())

        val declaredProperties = target.declaredMemberProperties
            .filter { it.hasAnnotation<EnvProperty>() }
            .associateBy { it.name }

        val valueProvider = ConstructorParameterValueProvider(
            converters = config.effectiveConverters,
            emptyValueStrategy = config.emptyValueStrategy,
        )

        val parameters = constructor.parameters.mapNotNull { parameter ->
            val property = declaredProperties[parameter.name] ?: return@mapNotNull null
            val annotation = property.findAnnotation<EnvProperty>() ?: return@mapNotNull null

            val envValue = envValues[annotation.key]
            if (annotation.required && envValue.isNullOrBlank()) {
                throw MissingRequiredPropertyException("Required environment variable ${annotation.key} is not set")
            }

            if (envValue == null && parameter.isOptional) {
                return@mapNotNull null
            }

            val value = valueProvider.provide(value = envValue, parameter = parameter)

            if (value == null && parameter.isOptional) {
                return@mapNotNull null
            }

            parameter to value
        }

        return constructor.callBy(parameters.toMap())
    }
}
