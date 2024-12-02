package io.github.kronst.kenv.core.converter

import kotlin.reflect.KType

interface ValueConverter {

    fun canConvert(type: KType): Boolean

    fun convert(value: String, type: KType): Any?
}
