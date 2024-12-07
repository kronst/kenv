package io.github.kronst.kenv.config

import io.github.kronst.kenv.core.converter.CollectionValueConverter
import io.github.kronst.kenv.core.converter.PrimitiveValueConverter
import io.github.kronst.kenv.core.converter.StringValueConverter
import io.github.kronst.kenv.core.converter.ValueConverter

/**
 * Configuration for Kenv.
 *
 * @property fileName The name of the file to load environment variables from
 * @property path The path to the file to load environment variables from
 * @property profile The profile to load environment variables from. For example for profile `dev`, the file will be named `.env.dev`
 * @property emptyValueStrategy The strategy to use when an environment variable is empty and the property has no default value in the constructor
 * @property collectionItemSeparator The separator to use when converting collection types (List, Set)
 * @property mapItemSeparator The separator for map entries when converting Map types
 * @property mapKeyValueSeparator The separator for map key-value pairs when converting Map types
 */
data class KenvConfig(
    val fileName: String = ".env",
    val path: String = ".",
    val profile: String? = null,
    val emptyValueStrategy: EmptyValueStrategy = EmptyValueStrategy.DEFAULT,
    val collectionItemSeparator: String = ",",
    val mapItemSeparator: String = ";",
    val mapKeyValueSeparator: String = "=",
    private val converters: List<ValueConverter>? = null,
) {

    val effectiveConverters: List<ValueConverter>

    init {
        effectiveConverters = converters ?: initDefaultConverters()
    }

    fun getFullEnvFilePath(): String {
        val profileSuffix = profile?.let { ".$it" } ?: ""
        return "$path/$fileName$profileSuffix".replace("//", "/")
    }

    private fun initDefaultConverters(): List<ValueConverter> {
        return listOf(
            StringValueConverter(),
            PrimitiveValueConverter(),
            CollectionValueConverter(
                converters = listOf(
                    StringValueConverter(),
                    PrimitiveValueConverter(),
                ),
                separator = collectionItemSeparator
            )
        )
    }
}
