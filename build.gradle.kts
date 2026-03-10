plugins {
    id("com.android.application") version "8.8.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
}


tasks.register("resolveAllDependencies") {
    group = "verification"
    description = "Resolve all resolvable configurations from all projects."

    doLast {
        rootProject.allprojects.forEach { project ->
            project.configurations
                .filter { it.isCanBeResolved }
                .forEach { config ->
                    config.resolve()
                }
        }
    }
}
