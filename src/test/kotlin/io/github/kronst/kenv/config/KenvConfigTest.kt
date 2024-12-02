package io.github.kronst.kenv.config

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KenvConfigTest {

    @Test
    fun `when custom file name then build correct full file path`() {
        val config = KenvConfig(
            fileName = ".custom",
        )

        assertEquals("./.custom", config.getFullEnvFilePath())
    }

    @Test
    fun `when custom path then build correct full file path`() {
        val config = KenvConfig(
            path = "/custom/config/path",
        )

        assertEquals("/custom/config/path/.env", config.getFullEnvFilePath())
    }

    @Test
    fun `when custom file name and path then build correct full file path`() {
        val config = KenvConfig(
            fileName = ".config",
            path = "/custom/path",
        )

        assertEquals("/custom/path/.config", config.getFullEnvFilePath())
    }

    @Test
    fun `when profile specified then build correct full file path`() {
        val config = KenvConfig(
            profile = "production"
        )

        assertEquals("./.env.production", config.getFullEnvFilePath())
    }
}
