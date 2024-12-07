package io.github.kronst.kenv.core

import io.github.kronst.kenv.config.EmptyValueStrategy
import io.github.kronst.kenv.config.KenvConfig
import io.github.kronst.kenv.exception.MissingConverterImplementationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.primaryConstructor

class ConstructorParameterValueProviderTest {

    private val config = KenvConfig()

    private val emptyDefaultProvider = ConstructorParameterValueProvider(
        converter = config.effectiveConverter,
        emptyValueStrategy = EmptyValueStrategy.DEFAULT,
    )
    private val emptyNullProvider = ConstructorParameterValueProvider(
        converter = config.effectiveConverter,
        emptyValueStrategy = EmptyValueStrategy.NULL,
    )

    @Test
    fun `when empty value for non-nullable parameter and NULL strategy then throw exception`() {
        data class Dummy(val value: Int)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        assertThrows<IllegalStateException> { emptyNullProvider.provide(value = "", parameter = parameter) }
    }

    @Test
    fun `when empty value for nullable parameter and NULL strategy then return null`() {
        data class Dummy(val value: Int?)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        assertNull(emptyNullProvider.provide(value = "", parameter = parameter))
    }

    @Test
    fun `when empty value for non-nullable parameter and DEFAULT strategy then return default value`() {
        data class Dummy(val value: Int)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        val value = emptyDefaultProvider.provide(value = "", parameter = parameter)
        assertInstanceOf(Int::class.javaObjectType, value)
        assertEquals(0, value)
    }

    @Test
    fun `when empty value for nullable parameter and DEFAULT strategy then return null`() {
        data class Dummy(val valueInt: Int?)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        val value = emptyDefaultProvider.provide(value = "", parameter = parameter)
        assertNull(value)
    }

    @Test
    fun `when no converter found then throw exception`() {
        data class Dummy(val value: Any)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        assertThrows<MissingConverterImplementationException> {
            emptyDefaultProvider.provide(value = "42", parameter = parameter)
        }

        assertThrows<MissingConverterImplementationException> {
            emptyNullProvider.provide(value = "42", parameter = parameter)
        }
    }

    @Test
    fun `when converter found then use it to provide value`() {
        data class Dummy(val value: Int)

        val parameter = Dummy::class.primaryConstructor!!.parameters.first()

        val value = emptyDefaultProvider.provide(value = "42", parameter = parameter)

        assertInstanceOf(Int::class.javaObjectType, value)
        assertEquals(42, value)
    }
}
