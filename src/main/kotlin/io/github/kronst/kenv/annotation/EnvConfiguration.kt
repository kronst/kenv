package io.github.kronst.kenv.annotation

/**
 * Marks a class as a configuration class that can be loaded with [Kenv][io.github.kronst.kenv.core.Kenv].
 * This annotation is required for any class that should be loaded from environment variables.
 *
 * Example:
 * ```
 * @EnvConfiguration
 * data class AppConfig(
 *     @EnvProperty("APP_NAME")
 *     val name: String,
 *
 *     @EnvProperty("APP_PORT")
 *     val port: Int
 * )
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnvConfiguration
