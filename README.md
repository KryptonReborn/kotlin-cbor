# KOTLIN cbor

[![GitHub release](https://img.shields.io/badge/release-v0.0.1-blue.svg)](https://github.com/KryptonReborn/kotlin-cbor/releases/tag/v0.0.1) [![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.23-B125EA?logo=kotlin)](https://kotlinlang.org)
[![Build Status](https://github.com/saschpe/kase64/workflows/Main/badge.svg)](https://github.com/KryptonReborn/kotlin-cbor/actions)
[![License](http://img.shields.io/:License-Apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
![coverage badge](https://img.shields.io/endpoint?url=https://gist.githubusercontent.com/iTanChi/80bcc643522fc574144cb35911894d21/raw/kotlin-cbor-coverage-badge.json)
![badge-android](http://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android)
![badge-ios](http://img.shields.io/badge/Platform-iOS-orange.svg?logo=apple)
![badge-js](http://img.shields.io/badge/Platform-NodeJS-yellow.svg?logo=javascript)
![badge-jvm](http://img.shields.io/badge/Platform-JVM-red.svg?logo=openjdk)
![badge-linux](http://img.shields.io/badge/Platform-Linux-lightgrey.svg?logo=linux)
![badge-macos](http://img.shields.io/badge/Platform-macOS-orange.svg?logo=apple)
![badge-windows](http://img.shields.io/badge/Platform-Windows-blue.svg?logo=windows)

[//]: # (![badge-tvos]&#40;http://img.shields.io/badge/Platform-tvOS-orange.svg?logo=apple&#41;)

[//]: # (![badge-watchos]&#40;http://img.shields.io/badge/Platform-watchOS-orange.svg?logo=apple&#41;)

Kotlin implementation of RFC 7049: Concise Binary Object Representation (CBOR)

## Download

You must use a personal access token (classic) with the appropriate scopes to publish and install
packages
in [GitHub Packages](https://docs.github.com/en/packages/learn-github-packages/introduction-to-github-packages#authenticating-to-github-packages).

Add the following repository to your settings.gradle.kts file

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/KryptonReborn/kotlin-cbor")
        credentials {
            username = "user name"
            password = "personal access token"
        }
    }
}
```

Add the following dependency to your build.gradle.kts file

```build.gradle.kts
dependencies {
    implementation("dev.kryptonreborn.cbor:cbor:[version]")
}
```

## Acknowledgements

This library is inspired by [cbor-java](https://github.com/c-rack/cbor-java) by [c-rack](https://github.com/c-rack).