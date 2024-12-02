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
    fun `when config class not annotated with EnvConfiguration then throw exception`(@TempDir dir: Path) {
        val env = dir.resolve(".env")
        env.toFile().writeText("DATASOURCE_HOST=localhost")

        data class DatasourceConfig(
            @EnvProperty(key = "DATASOURCE_HOST")
            val host: String
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
            val port: Int
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
            val port: Int
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
            val user: String?
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
            val user: String = "postgres"
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
            val user: String
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
            val credentials: Credentials = Credentials()
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
            val credentials: Credentials? = Credentials()
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
            val credentials: Credentials = Credentials()
        )

        val config = KenvConfig(
            path = env.parent.toString(),
            emptyValueStrategy = EmptyValueStrategy.DEFAULT,
        )

        val exception = assertThrows<IllegalStateException> { Kenv.load<DatasourceConfig>(config) }
        assertTrue(exception.message!!.contains("Cannot provide a null value for non-nullable parameter credentials"))
    }
}
