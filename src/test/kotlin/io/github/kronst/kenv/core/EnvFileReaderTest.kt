package io.github.kronst.kenv.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.assertEquals

class EnvFileReaderTest {

    private val reader = EnvFileReader()

    @Test
    fun `when file does not exist then throw exception`() {
        assertThrows<IllegalArgumentException> { reader.read("non-existing-file.env") }
    }

    @Test
    fun `when valid content format then read envs correctly`(@TempDir dir: Path) {
        val file = dir.resolve(".env")
        file.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        val values = reader.read(file.toString())

        assertEquals(2, values.size)
        assertEquals("localhost", values["DATASOURCE_HOST"])
        assertEquals("5432", values["DATASOURCE_PORT"])
    }

    @Test
    fun `when invalid content then throw exception`(@TempDir dir: Path) {
        val file = dir.resolve(".env")
        file.toFile().writeText(
            """
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
                INVALID_LINE
            """.trimIndent()
        )

        assertThrows<IllegalArgumentException> { reader.read(file.toString()) }
    }

    @Test
    fun `when comments present then read envs correctly`(@TempDir dir: Path) {
        val file = dir.resolve(".env")
        file.toFile().writeText(
            """
                # Database configuration
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
            """.trimIndent()
        )

        val values = reader.read(file.toString())

        assertEquals(2, values.size)
        assertEquals("localhost", values["DATASOURCE_HOST"])
        assertEquals("5432", values["DATASOURCE_PORT"])
    }

    @Test
    fun `when empty lines present then read envs correctly`(@TempDir dir: Path) {
        val file = dir.resolve(".env")
        file.toFile().writeText(
            """
                # Database configuration
                DATASOURCE_HOST=localhost
                DATASOURCE_PORT=5432
                
                # Server configuration
                SERVER_PORT=8080
            """.trimIndent()
        )

        val values = reader.read(file.toString())

        assertEquals(3, values.size)
        assertEquals("localhost", values["DATASOURCE_HOST"])
        assertEquals("5432", values["DATASOURCE_PORT"])
        assertEquals("8080", values["SERVER_PORT"])
    }
}
