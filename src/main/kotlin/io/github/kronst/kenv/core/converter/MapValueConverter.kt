package io.github.kronst.kenv.core.converter

import kotlin.reflect.KType

class MapValueConverter : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return type.classifier in supportedTypes
    }

    override fun convert(value: String, type: KType): Any? {
        TODO("Not yet implemented")
    }

    companion object {
        private val supportedTypes = setOf(
            Map::class,
        )
    }
}
