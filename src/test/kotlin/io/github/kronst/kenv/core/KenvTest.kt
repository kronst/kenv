package io.github.kronst.kenv.core

import io.github.kronst.kenv.annotation.EnvConfiguration
import io.github.kronst.kenv.annotation.EnvProperty
import io.github.kronst.kenv.config.EmptyValueStrategy
import io.github.kronst.kenv.config.KenvConfig
import io.github.kronst.kenv.exception.MissingRequiredPropertyException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class KenvTest {

    @Test
    fun `check config loaded correctly`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                BYTE_VALUE=42
                SHORT_VALUE=42
                INT_VALUE=42
                LONG_VALUE=42
                FLOAT_VALUE=42.0
                DOUBLE_VALUE=42.0
                BOOLEAN_VALUE=true
                CHAR_VALUE=a
                STRING_VALUE=foo
                
                LIST_BYTE=42, 43, 44
                LIST_SHORT=42, 43, 44
                LIST_INT=42, 43, 44
                LIST_LONG=42, 43, 44
                LIST_FLOAT=42.0, 43.0, 44.0
                LIST_DOUBLE=42.0, 43.0, 44.0
                LIST_BOOLEAN=true, false, true
                LIST_CHAR=a, b, c
                LIST_STRING=foo, bar, baz
                
                SET_BYTE=42, 43, 44, 42
                SET_SHORT=42, 43, 44, 42
                SET_INT=42, 43, 44, 42
                SET_LONG=42, 43, 44, 42
                SET_FLOAT=42.0, 43.0, 44.0, 42.0
                SET_DOUBLE=42.0, 43.0, 44.0, 42.0
                SET_BOOLEAN=true, false, true, true
                SET_CHAR=a, b, c, a
                SET_STRING=foo, bar, baz, foo
                
                MAP_STRING_INT=key1=42; key2=43; key3=44
                MAP_STRING_STRING=key1=value1; key2=value2; key3=value3
                MAP_STRING_LIST_STRING=key1=a, b, c; key2=d, e, f; key3=g, h, i
                MAP_INT_SET_STRING=1=a, a, b, c; 2=d, e, e, f; 3=g, h, i, i
            """.trimIndent()
        )

        @EnvConfiguration
        class Config(
            @EnvProperty(key = "BYTE_VALUE") val byteValue: Byte,
            @EnvProperty(key = "SHORT_VALUE") val shortValue: Short,
            @EnvProperty(key = "INT_VALUE") val intValue: Int,
            @EnvProperty(key = "LONG_VALUE") val longValue: Long,
            @EnvProperty(key = "FLOAT_VALUE") val floatValue: Float,
            @EnvProperty(key = "DOUBLE_VALUE") val doubleValue: Double,
            @EnvProperty(key = "BOOLEAN_VALUE") val booleanValue: Boolean,
            @EnvProperty(key = "CHAR_VALUE") val charValue: Char,
            @EnvProperty(key = "STRING_VALUE") val stringValue: String,
            @EnvProperty(key = "LIST_BYTE") val listByte: List<Byte>,
            @EnvProperty(key = "LIST_SHORT") val listShort: List<Short>,
            @EnvProperty(key = "LIST_INT") val listInt: List<Int>,
            @EnvProperty(key = "LIST_LONG") val listLong: List<Long>,
            @EnvProperty(key = "LIST_FLOAT") val listFloat: List<Float>,
            @EnvProperty(key = "LIST_DOUBLE") val listDouble: List<Double>,
            @EnvProperty(key = "LIST_BOOLEAN") val listBoolean: List<Boolean>,
            @EnvProperty(key = "LIST_CHAR") val listChar: List<Char>,
            @EnvProperty(key = "LIST_STRING") val listString: List<String>,
            @EnvProperty(key = "SET_BYTE") val setByte: Set<Byte>,
            @EnvProperty(key = "SET_SHORT") val setShort: Set<Short>,
            @EnvProperty(key = "SET_INT") val setInt: Set<Int>,
            @EnvProperty(key = "SET_LONG") val setLong: Set<Long>,
            @EnvProperty(key = "SET_FLOAT") val setFloat: Set<Float>,
            @EnvProperty(key = "SET_DOUBLE") val setDouble: Set<Double>,
            @EnvProperty(key = "SET_BOOLEAN") val setBoolean: Set<Boolean>,
            @EnvProperty(key = "SET_CHAR") val setChar: Set<Char>,
            @EnvProperty(key = "SET_STRING") val setString: Set<String>,
            @EnvProperty(key = "MAP_STRING_INT") val mapStringInt: Map<String, Int>,
            @EnvProperty(key = "MAP_STRING_STRING") val mapStringString: Map<String, String>,
            @EnvProperty(key = "MAP_STRING_LIST_STRING") val mapStringListString: Map<String, List<String>>,
            @EnvProperty(key = "MAP_INT_SET_STRING") val mapIntSetString: Map<Int, Set<String>>,
        )

        val config = Kenv.load<Config>(KenvConfig(path = env.parent.toString()))

        assertEquals(42.toByte(), config.byteValue)
        assertEquals(42.toShort(), config.shortValue)
        assertEquals(42, config.intValue)
        assertEquals(42L, config.longValue)
        assertEquals(42.0f, config.floatValue)
        assertEquals(42.0, config.doubleValue)
        assertEquals(true, config.booleanValue)
        assertEquals('a', config.charValue)
        assertEquals("foo", config.stringValue)

        assertEquals(listOf<Byte>(42, 43, 44), config.listByte)
        assertEquals(listOf<Short>(42, 43, 44), config.listShort)
        assertEquals(listOf(42, 43, 44), config.listInt)
        assertEquals(listOf(42L, 43L, 44L), config.listLong)
        assertEquals(listOf(42.0f, 43.0f, 44.0f), config.listFloat)
        assertEquals(listOf(42.0, 43.0, 44.0), config.listDouble)
        assertEquals(listOf(true, false, true), config.listBoolean)
        assertEquals(listOf('a', 'b', 'c'), config.listChar)
        assertEquals(listOf("foo", "bar", "baz"), config.listString)

        assertEquals(setOf<Byte>(42, 43, 44), config.setByte)
        assertEquals(setOf<Short>(42, 43, 44), config.setShort)
        assertEquals(setOf(42, 43, 44), config.setInt)
        assertEquals(setOf(42L, 43L, 44L), config.setLong)
        assertEquals(setOf(42.0f, 43.0f, 44.0f), config.setFloat)
        assertEquals(setOf(42.0, 43.0, 44.0), config.setDouble)
        assertEquals(setOf(true, false), config.setBoolean)
        assertEquals(setOf('a', 'b', 'c'), config.setChar)
        assertEquals(setOf("foo", "bar", "baz"), config.setString)

        assertEquals(
            mapOf(
                "key1" to 42,
                "key2" to 43,
                "key3" to 44
            ),
            config.mapStringInt
        )
        assertEquals(
            mapOf(
                "key1" to "value1",
                "key2" to "value2",
                "key3" to "value3"
            ),
            config.mapStringString
        )
        assertEquals(
            mapOf(
                "key1" to listOf("a", "b", "c"),
                "key2" to listOf("d", "e", "f"),
                "key3" to listOf("g", "h", "i")
            ),
            config.mapStringListString
        )
        assertEquals(
            mapOf(
                1 to setOf("a", "b", "c"),
                2 to setOf("d", "e", "f"),
                3 to setOf("g", "h", "i")
            ),
            config.mapIntSetString
        )
    }

    @Test
    fun `when config class not annotated with EnvConfiguration then throw exception`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText("DATASOURCE_HOST=localhost")

        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST")
            val host: String,
        )

        val config = KenvConfig(path = env.parent.toString())

        val exception = assertThrows<IllegalArgumentException> { Kenv.load<DatasourceConfig>(config) }
        assertTrue(exception.message!!.contains("must be annotated with @EnvConfiguration"))
    }

    @Test
    fun `when config class has no primary constructor then throw exception`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText("DATASOURCE_HOST=localhost")

        @EnvConfiguration
        class DatasourceConfig {
            @EnvProperty(key = "DATASOURCE_HOST")
            var host: String = "localhost"

            constructor(host: String) {
                this.host = host
            }
        }

        val config = KenvConfig(path = env.parent.toString())

        val exception = assertThrows<IllegalArgumentException> { Kenv.load<DatasourceConfig>(config) }
        assertTrue(exception.message!!.contains("must have a primary constructor"))
    }

    @Test
    fun `when property required but does not exist in env file then throw exception`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
            """.trimIndent()
        )

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST", required = true)
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT", required = true)
            val port: Int,
        )

        val config = KenvConfig(path = env.parent.toString())

        val exception = assertThrows<MissingRequiredPropertyException> { Kenv.load<DatasourceConfig>(config) }
        assertEquals("Required environment variable DATASOURCE_PORT is not set", exception.message)
    }

    @Test
    fun `when property not required and not in env file then set value from DEFAULT provider`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
            """.trimIndent()
        )

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST", required = false)
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT", required = false)
            val port: Int,
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.DEFAULT,
        )
        val datasourceConfig = Kenv.load<DatasourceConfig>(config)

        assertEquals("localhost", datasourceConfig.host)
        assertEquals(0, datasourceConfig.port)
    }

    @Test
    fun `when property not required and not in env file then set value from NULL provider`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST", required = false)
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT", required = false)
            val port: Int,

            @EnvProperty(key = "DATASOURCE_USER", required = false)
            val user: String?,
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.NULL,
        )
        val datasourceConfig = Kenv.load<DatasourceConfig>(config)

        assertEquals("localhost", datasourceConfig.host)
        assertEquals(5432, datasourceConfig.port)
        assertNull(datasourceConfig.user)
    }

    @Test
    fun `when property not required and not in env file then use default value from constructor`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST", required = false)
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT", required = false)
            val port: Int,

            @EnvProperty(key = "DATASOURCE_USER", required = false)
            val user: String = "postgres",
        )

        val config = KenvConfig(path = env.parent.toString())
        val datasourceConfig = Kenv.load<DatasourceConfig>(config)

        assertEquals("localhost", datasourceConfig.host)
        assertEquals(5432, datasourceConfig.port)
        assertEquals("postgres", datasourceConfig.user)
    }

    @Test
    fun `when non-null property not required then NULL provider throw exception if empty`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST", required = false)
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT", required = false)
            val port: Int,

            @EnvProperty(key = "DATASOURCE_USER", required = false)
            val user: String,
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.NULL,
        )

        val exception = assertThrows<IllegalStateException> { Kenv.load<DatasourceConfig>(config) }
        assertTrue(exception.message!!.contains("Cannot provide a null value for non-nullable parameter user"))
    }

    @Test
    fun `when no key in env file use default value from constructor`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        data class Credentials(val username: String = "postgres", val password: String = "password")

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST")
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT")
            val port: Int,

            @EnvProperty(key = "DATASOURCE_CREDENTIALS")
            val credentials: Credentials = Credentials(),
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.DEFAULT,
        )

        val datasourceConfig = Kenv.load<DatasourceConfig>(config)

        assertEquals("localhost", datasourceConfig.host)
        assertEquals(5432, datasourceConfig.port)
        assertEquals("postgres", datasourceConfig.credentials.username)
        assertEquals("password", datasourceConfig.credentials.password)
    }

    @Test
    fun `when empty value for nullable optional parameter then use default value from constructor`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
                DATASOURCE_CREDENTIALS=
            """.trimIndent()
        )

        data class Credentials(val username: String = "postgres", val password: String = "password")

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST")
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT")
            val port: Int,

            @EnvProperty(key = "DATASOURCE_CREDENTIALS", required = false)
            val credentials: Credentials? = Credentials(),
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.DEFAULT,
        )

        val datasourceConfig = Kenv.load<DatasourceConfig>(config)

        assertEquals("localhost", datasourceConfig.host)
        assertEquals(5432, datasourceConfig.port)
        assertEquals("postgres", datasourceConfig.credentials?.username)
        assertEquals("password", datasourceConfig.credentials?.password)
    }

    @Test
    fun `when empty value for non-null optional parameter then throw exception`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
                DATASOURCE_CREDENTIALS=
            """.trimIndent()
        )

        data class Credentials(val username: String = "postgres", val password: String = "password")

        @EnvConfiguration
        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST")
            val host: String,

            @EnvProperty(key = "DATASOURCE_PORT")
            val port: Int,

            @EnvProperty(key = "DATASOURCE_CREDENTIALS", required = false)
            val credentials: Credentials = Credentials(),
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.DEFAULT,
        )

        val exception = assertThrows<IllegalStateException> { Kenv.load<DatasourceConfig>(config) }
        assertTrue(exception.message!!.contains("Cannot provide a null value for non-nullable parameter credentials"))
    }
}
