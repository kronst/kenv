package io.github.kronst.kenv.core

import kotlin.reflect.KType

object DefaultValueProvider {

    fun provide(type: KType): Any? {
        return when (type.classifier) {
            Byte::class -> 0.toByte()
            Short::class -> 0.toShort()
            Int::class -> 0
            Long::class -> 0L
            Float::class -> 0.0f
            Double::class -> 0.0
            Boolean::class -> false
            Char::class -> '\u0000'
            String::class -> ""
            List::class -> emptyList<Nothing>()
            Set::class -> emptySet<Nothing>()
            Map::class -> emptyMap<Nothing, Nothing>()
            else -> null
        }
    }
}
