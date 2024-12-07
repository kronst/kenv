package io.github.kronst.kenv.core.converter

import io.github.kronst.kenv.config.KenvConfig
import io.github.kronst.kenv.exception.MissingConverterImplementationException
import kotlin.reflect.KType

class CompositeValueConverter(
    private val converters: List<ValueConverter>,
) : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return converters.any { it.canConvert(type) }
    }

    override fun convert(value: String, type: KType): Any? {
        val converter = converters.find { it.canConvert(type) }
            ?: throw MissingConverterImplementationException("No value converter found for type $type")

        return converter.convert(value = value, type = type)
    }

    data class Builder(
        private var collectionItemSeparator: String = KenvConfig.DEFAULT_COLLECTION_ITEM_SEPARATOR,
        private var mapItemSeparator: String = KenvConfig.DEFAULT_MAP_ITEM_SEPARATOR,
        private var mapKeyValueSeparator: String = KenvConfig.DEFAULT_MAP_KEY_VALUE_SEPARATOR,
    ) {

        private val baseConverters = mutableListOf<ValueConverter>()

        fun addBaseConverter(converter: ValueConverter) = apply { baseConverters.add(converter) }

        fun collectionItemSeparator(separator: String) = apply { collectionItemSeparator = separator }

        fun mapItemSeparator(separator: String) = apply { mapItemSeparator = separator }

        fun mapKeyValueSeparator(separator: String) = apply { mapKeyValueSeparator = separator }

        fun build(): CompositeValueConverter {
            val converters = baseConverters.toList()
            val baseComposite = CompositeValueConverter(converters)

            val collectionConverter = CollectionValueConverter(
                converter = baseComposite,
                separator = collectionItemSeparator,
            )

            val mapConverter = MapValueConverter(
                converter = CompositeValueConverter(converters + collectionConverter),
                itemSeparator = mapItemSeparator,
                keyValueSeparator = mapKeyValueSeparator,
            )

            return CompositeValueConverter(
                converters = converters + collectionConverter + mapConverter
            )
        }
    }
}
