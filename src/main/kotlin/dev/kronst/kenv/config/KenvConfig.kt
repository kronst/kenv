package dev.kronst.kenv.config

import dev.kronst.kenv.core.converter.PrimitiveValueConverter
import dev.kronst.kenv.core.converter.StringValueConverter
import dev.kronst.kenv.core.converter.ValueConverter

/**
 * Configuration for Kenv.
 * @property fileName The name of the file to load environment variables from
 * @property path The path to the file to load environment variables from
 * @property profile The profile to load environment variables from. For example for profile `dev`, the file will be named `.env.dev`
 * @property emptyValueStrategy The strategy to use when an environment variable is empty and the property has no default value in the constructor
 */
data class KenvConfig(
    val fileName: String = ".env",
    val path: String = ".",
    val profile: String? = null,
    val converters: List<ValueConverter> = DEFAULT_VALUE_CONVERTERS,
    val emptyValueStrategy: EmptyValueStrategy = EmptyValueStrategy.DEFAULT
) {

    fun getFullEnvFilePath(): String {
        val profileSuffix = profile?.let { ".$it" } ?: ""
        return "$path/$fileName$profileSuffix".replace("//", "/")
    }

    companion object {
        val DEFAULT_VALUE_CONVERTERS = listOf(
            StringValueConverter(),
            PrimitiveValueConverter(),
        )
    }
}
