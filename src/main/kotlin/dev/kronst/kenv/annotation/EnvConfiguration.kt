package dev.kronst.kenv.annotation

/**
 * Marks a class as a configuration class that can be loaded with [Kenv][dev.kronst.kenv.core.Kenv].
 * This annotation is required for any class that should be loaded from environment variables.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnvConfiguration
