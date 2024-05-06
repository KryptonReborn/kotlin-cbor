package plugins

import com.android.build.gradle.LibraryExtension
import extensions.libs
import org.gradle.api.*
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

class CommonMppLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("androidCompileSdk").get().displayName.toInt()
                defaultConfig {
                    minSdk = libs.findVersion("androidMinSdk").get().displayName.toInt()
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                jvm {
                    compilations.all {
                        kotlinOptions.jvmTarget = "17"
                    }
                    testRuns["test"].executionTask.configure {
                        useJUnitPlatform()
                    }
                }
                androidTarget {
                    publishAllLibraryVariants()
                    publishLibraryVariantsGroupedByFlavor = true
                    compilations.all {
                        kotlinOptions.jvmTarget = "17"
                    }
                }

                js {
                    configureTargetsForJS()
                }
                @OptIn(ExperimentalWasmDsl::class)
                wasmJs {
                    configureTargetsForJS()
                }
//kotlinBignum does not yet support wasmWasi
//                @OptIn(ExperimentalWasmDsl::class)
//                wasmWasi {
//                    nodejs()
//                }

                iosArm64()
                iosX64()
                iosSimulatorArm64()

                mingwX64()
                macosX64()
                macosArm64()
                linuxX64()
                linuxArm64()

                applyDefaultHierarchyTemplate()

                sourceSets.apply {
                    commonMain.get()
                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlinTest").get())
                    }
                    nativeMain.get().dependsOn(commonMain.get())
                    nativeTest.get().dependsOn(commonTest.get())
                    iosMain.get().dependsOn(nativeMain.get())
                    iosTest.get().dependsOn(nativeTest.get())
                }
            }
        }
    }

    private fun KotlinJsSubTargetDsl.configureMochaTimeout() {
        testTask {
            useMocha {
                timeout = "20s"
            }
        }
    }

    private fun KotlinJsTargetDsl.configureTargetsForJS() {
        browser {
            configureMochaTimeout()
        }
        nodejs {
            configureMochaTimeout()
        }
        binaries.executable()
    }
}
