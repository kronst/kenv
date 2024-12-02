package io.github.kronst.kenv.core.converter

import kotlin.reflect.KType

class CollectionValueConverter : ValueConverter {

    override fun canConvert(type: KType): Boolean {
        return type.classifier in supportedTypes
    }

    override fun convert(value: String, type: KType): Any? {
        TODO("Not yet implemented")
    }

    companion object {
        private val supportedTypes = setOf(
            Array::class,
            List::class,
            Set::class,
        )
    }
}
