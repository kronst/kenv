package dev.kronst.kenv.annotation

/**
 * Specifies the mapping between a property and an environment variable.
 * Note: All properties must be declared in the primary constructor.
 * Properties with [@EnvProperty][EnvProperty] annotation outside the constructor **WILL BE IGNORED**
 * which can lead to unexpected behavior.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class EnvProperty(
    /**
     * The name of the environment variable
     */
    val key: String,

    /**
     * Indicates if the environment variable must be present. If `true` and the environment variable is not set,
     * an [MissingRequiredPropertyException][dev.kronst.kenv.exception.MissingRequiredPropertyException] will be thrown
     */
    val required: Boolean = false,
)
