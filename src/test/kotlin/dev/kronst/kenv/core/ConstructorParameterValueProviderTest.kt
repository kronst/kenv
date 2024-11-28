package dev.kronst.kenv.core

import dev.kronst.kenv.config.EmptyValueStrategy
import dev.kronst.kenv.config.KenvConfig
import dev.kronst.kenv.exception.UnsupportedValueTypeException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.primaryConstructor

class ConstructorParameterValueProviderTest {

    private val emptyDefaultProvider = ConstructorParameterValueProvider(
        converters = KenvConfig.DEFAULT_VALUE_CONVERTERS,
        emptyValueStrategy = EmptyValueStrategy.DEFAULT,
    )
    private val emptyNullProvider = ConstructorParameterValueProvider(
        converters = KenvConfig.DEFAULT_VALUE_CONVERTERS,
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

        assertThrows<UnsupportedValueTypeException> {
            emptyDefaultProvider.provide(value = "42", parameter = parameter)
        }

        assertThrows<UnsupportedValueTypeException> {
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
