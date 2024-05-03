plugins {
    id(libs.plugins.commonMppLib.get().pluginId)
    id(libs.plugins.commonMppPublish.get().pluginId)
}

publishConfig {
    url = "https://maven.pkg.github.com/KryptonReborn/kotlin-cbor"
    groupId = "kotlin-cbor"
    artifactId = "library"
}

version = "0.0.1"

android {
    namespace = "kotlin.cbor"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinStdLib)
            }
        }
    }
}
