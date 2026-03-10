pluginManagement {
    val enterpriseMirror = System.getenv("SECUREKIOSK_MAVEN_MIRROR")
    repositories {
        if (!enterpriseMirror.isNullOrBlank()) {
            maven(url = enterpriseMirror)
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.android.application" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "org.jetbrains.kotlin.android" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "com.google.dagger.hilt.android" -> useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
                "com.google.devtools.ksp" -> useModule("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    val enterpriseMirror = System.getenv("SECUREKIOSK_MAVEN_MIRROR")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (!enterpriseMirror.isNullOrBlank()) {
            maven(url = enterpriseMirror)
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "SecureKioskV2"
include(":app")
