package io.github.kronst.kenv.core.converter

import kotlin.reflect.KType

class StringValueConverter : ValueConverter {
    override fun canConvert(type: KType): Boolean {
        return type.classifier == String::class
    }

    override fun convert(value: String, type: KType): Any {
        return value
    }
}
