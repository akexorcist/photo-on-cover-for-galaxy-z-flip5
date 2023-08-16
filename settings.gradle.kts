pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Photo on Cover"
include(":app")
include(":feature:widget")
include(":feature:home")
include(":base:resource")
include(":base:ui")
include(":base:core")
include(":feature:instruction")
