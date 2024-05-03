plugins {
    id(libs.plugins.commonMppLib.get().pluginId)
}

android {
    namespace = "dev.kryptonreborn.cbor"
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinxIo)
                implementation(libs.kotlinBignum)
            }
        }
    }
}
