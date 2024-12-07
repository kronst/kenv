package io.github.kronst.kenv.config

import io.github.kronst.kenv.core.converter.CompositeValueConverter
import io.github.kronst.kenv.core.converter.PrimitiveValueConverter
import io.github.kronst.kenv.core.converter.StringValueConverter

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
class KenvConfig(
    val fileName: String = ".env",
    val path: String = ".",
    val profile: String? = null,
    val emptyValueStrategy: EmptyValueStrategy = EmptyValueStrategy.DEFAULT,
    private val collectionItemSeparator: String = DEFAULT_COLLECTION_ITEM_SEPARATOR,
    private val mapItemSeparator: String = DEFAULT_MAP_ITEM_SEPARATOR,
    private val mapKeyValueSeparator: String = DEFAULT_MAP_KEY_VALUE_SEPARATOR,
    converter: CompositeValueConverter? = null,
) {

    val effectiveConverter: CompositeValueConverter

    init {
        effectiveConverter = converter ?: initDefaultConverters()
    }

    fun getFullEnvFilePath(): String {
        val profileSuffix = profile?.let { ".$it" } ?: ""
        return "$path/$fileName$profileSuffix".replace("//", "/")
    }

    private fun initDefaultConverters(): CompositeValueConverter {
        return CompositeValueConverter.Builder()
            .addBaseConverter(StringValueConverter())
            .addBaseConverter(PrimitiveValueConverter())
            .collectionItemSeparator(collectionItemSeparator)
            .mapItemSeparator(mapItemSeparator)
            .mapKeyValueSeparator(mapKeyValueSeparator)
            .build()
    }

    companion object {
        const val DEFAULT_COLLECTION_ITEM_SEPARATOR = ","
        const val DEFAULT_MAP_ITEM_SEPARATOR = ";"
        const val DEFAULT_MAP_KEY_VALUE_SEPARATOR = "="
    }
}
