import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    id(libs.plugins.commonMppLib.get().pluginId)
    id(libs.plugins.commonMppPublish.get().pluginId)
    id(libs.plugins.kover.get().pluginId)
}

publishConfig {
    url = "https://maven.pkg.github.com/KryptonReborn/kotlin-cbor"
    groupId = "dev.kryptonreborn.cbor"
    artifactId = "cbor"
}

version = "0.0.1"

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

rootProject.plugins.withType<YarnPlugin> {
    rootProject.configure<YarnRootExtension> {
        yarnLockMismatchReport = YarnLockMismatchReport.WARNING
        yarnLockAutoReplace = true
    }
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
}
