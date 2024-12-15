import org.jreleaser.model.Active

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jreleaser") version "1.16.0"
    `maven-publish`
    `java-library`
}

group = "io.github.kronst"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.kronst"
            artifactId = "kenv"

            from(components["java"])

            pom {
                name.set("kenv")
                description.set("Type-safe environment variables configuration library for Kotlin")
                url.set("https://github.com/kronst/kenv")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("kronst")
                        name.set("Roman Konstantynovskyi")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/kronst/kenv.git")
                    developerConnection.set("scm:git:ssh://github.com/kronst/kenv.git")
                    url.set("https://github.com/kronst/kenv")
                }
            }
        }
    }

    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

jreleaser {
    project {
        copyright.set("Roman Konstantynovskyi")
        description.set("Type-safe environment variables configuration library for Kotlin")
    }

    signing {
        active.set(Active.ALWAYS)
        armored.set(true)
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher/")
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}
