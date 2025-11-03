package com.example.spotifyrecommender

import java.io.File
import java.io.FileInputStream
import java.util.Properties

fun getLocalProperty(key: String): String {
    val properties = Properties()
    val localPropertiesFile = File("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties.getProperty(key, "")
}
