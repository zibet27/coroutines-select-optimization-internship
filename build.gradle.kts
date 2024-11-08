plugins {
    kotlin("multiplatform") version "2.0.20"
    id("org.jetbrains.kotlinx.atomicfu") version "0.25.0"
}

group = "org.internship"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


val hostOs: String = System.getProperty("os.name")
val isArm64 = System.getProperty("os.arch") == "aarch64"
val isMingwX64 = hostOs.startsWith("Windows")

kotlin {
    jvm()
    js(IR) {
        nodejs {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    when {
        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
        hostOs == "Linux" && isArm64 -> linuxArm64("native")
        hostOs == "Linux" && !isArm64 -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlin("stdlib"))
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
        }
        jvmTest.dependencies {
            implementation("org.jetbrains.kotlinx:lincheck:2.34")
        }

        jsMain.dependencies {
            api("org.jetbrains.kotlinx:atomicfu:0.25.0")
        }
        jsTest.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.9.0")
        }

        nativeMain.dependencies {
            api("org.jetbrains.kotlinx:atomicfu:0.25.0")
        }
    }
}