package io.github.kronst.kenv.core

import io.github.kronst.kenv.config.KenvConfig

object Kenv {

    /**
     * Loads environment variables into a target class.
     * @param config The configuration to use when loading the environment variables
     * @return The loaded class
     */
    inline fun <reified T : Any> load(config: KenvConfig = KenvConfig()): T {
        return KenvLoader.load(T::class, config)
    }
}
