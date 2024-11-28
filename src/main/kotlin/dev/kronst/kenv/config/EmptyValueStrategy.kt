package dev.kronst.kenv.config

enum class EmptyValueStrategy {
    /**
     * When an environment variable is empty:
     * - If property has a default value in constructor - it will be used.
     * - If property is nullable - it will be set to `null`.
     * - If property is not nullable - an [IllegalStateException] will be thrown.
     */
    NULL,

    /**
     * When an environment variable is empty:
     * - If property has a default value in constructor - it will be used.
     * - If property is nullable - it will be set to `null`.
     * - If property is not nullable - the default value will be used
     * (only for **primitives**, **strings**, **collections** and **maps**).
     */
    DEFAULT,
}
