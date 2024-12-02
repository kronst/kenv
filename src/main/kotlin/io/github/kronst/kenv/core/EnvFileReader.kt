package io.github.kronst.kenv.core

import java.nio.file.Paths
import kotlin.io.path.notExists
import kotlin.io.path.readLines

class EnvFileReader {

    fun read(path: String): Map<String, String> {
        val file = Paths.get(path)
        if (file.notExists()) {
            throw IllegalArgumentException("File '$path' does not exist")
        }

        return file.readLines()
            .filter { it.isNotBlank() && !it.startsWith("#") }
            .associate { line ->
                val parts = line.split("=", limit = 2)
                if (parts.size != 2) {
                    throw IllegalArgumentException("Invalid line in '$path': $line")
                }

                parts[0].trim() to parts[1].trim()
            }
    }
}
