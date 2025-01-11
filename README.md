# kenv

Type-safe environment variables configuration library for Kotlin.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.kronst/kenv.svg)](https://central.sonatype.com/artifact/io.github.kronst/kenv)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Features

- Type-safe access to environment variables
- Validation of required variables at startup
- Support for default values
- Flexible type conversion system
- Support for different environments (profiles)

## Installation

```kotlin
dependencies {
    implementation("io.github.kronst:kenv:0.1.0")
}
```

## Quick Start

1. Create your configuration class:

```kotlin
@EnvConfiguration
data class AppConfig(
    @EnvProperty("APP_NAME", required = true)
    val name: String,
    
    @EnvProperty("APP_PORT")
    val port: Int = 8080,
    
    @EnvProperty("ALLOWED_ORIGINS")
    val allowedOrigins: List<String> = emptyList(),
    
    @EnvProperty("FEATURE_FLAGS")
    val featureFlags: Map<String, Boolean> = emptyMap()
)
```

2. Create an environment file (.env):

```properties
APP_NAME=MyApp
ALLOWED_ORIGINS=http://localhost:3000,https://example.com
FEATURE_FLAGS=dark_mode=true;beta_features=false
```

3. Load configuration:

```kotlin
val config = Kenv.load<AppConfig>()
```

## Supported Types

- Primitives: String, Byte, Short, Int, Long, Float, Double, Boolean, Char
- Collections: List, Set
- Maps with any supported types as keys and values
- Nullable types

## Next Steps

- Objects (including nested objects)
- Custom converters

## Advanced Usage

### Custom Environment Files

```kotlin
val config = Kenv.load<AppConfig>(
    KenvConfig(
        fileName = ".env",
        path = "config",
        profile = "dev"  // Will load from config/.env.dev
    )
)
```

### Collection Separators

```kotlin
val config = Kenv.load<AppConfig>(
    KenvConfig(
        collectionItemSeparator = ",",
        mapItemSeparator = ";",
        mapKeyValueSeparator = "="
    )
)
```

### Empty Value Handling

```kotlin
val config = Kenv.load<AppConfig>(
    KenvConfig(
        emptyValueStrategy = EmptyValueStrategy.DEFAULT  // or EmptyValueStrategy.NULL
    )
)
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
