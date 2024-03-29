import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.util.Date


val artifactName = "kushetka"
val artifactGroup = "com.fieldkt"
val artifactVersion = "0.0.2.DEV"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.21")
    // Get version from git tags
    id("fr.coppernic.versioning") version "3.1.2"
    // Documentation for our code
    id("org.jetbrains.dokka") version "0.9.17"
    // Publication to bintray
    id("com.jfrog.bintray") version "1.8.4"
    // Maven publication
    `maven-publish`
}

repositories {
    jcenter()
    mavenCentral()
}

object Versions {
    val ktor = "1.2.5"
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compile("io.ktor:ktor-client-core:${Versions.ktor}")
    compile("io.ktor:ktor-client-json:${Versions.ktor}")
    compile("io.ktor:ktor-client-jackson:${Versions.ktor}")
    compile("io.ktor:ktor-client-auth-jvm:${Versions.ktor}")
    compile("io.ktor:ktor-client-cio:${Versions.ktor}")
    compile("io.github.microutils:kotlin-logging:1.7.7")
    compile("org.slf4j:slf4j-api:1.7.5")
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    implementation("io.ktor:ktor-client-logging-jvm:${Versions.ktor}")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xuse-experimental=kotlin.Experimental"
        }
    }
    tasks.withType<Test> {
        testLogging {
            showStandardStreams = true
            events("standardOut", "started", "passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}


tasks {


    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        moduleName = rootProject.name
    }
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
    dependsOn(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val pomUrl = "https://github.com/Evyy/kushetka"
val pomScmUrl = "https://github.com/Evyy/kushetka"
val pomIssueUrl = "https://github.com/Evyy/kushetka"
val pomDesc = "..."
val pomScmConnection = "..."
val pomScmDevConnection = "..."

val githubRepo = "https://github.com/Evyy/kushetka"
val githubReadme = "https://github.com/Evyy/kushetka"

val pomLicenseName = "The Apache Software License, Version 2.0"
val pomLicenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
val pomLicenseDist = "repo"

val pomDeveloperId = "..."
val pomDeveloperName = "..."


versioning {
    releaseMode = "snapshot"
    snapshot = ".DEV"
}


publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = artifactGroup
            artifactId = artifactName
            // version is gotten from an external plugin
            version = artifactVersion
            // This is the main artifact
            from(components["java"])
            // We are adding documentation artifact
            artifact(dokkaJar)
            // And sources
            artifact(sourcesJar)

            pom.withXml {
                asNode().apply {
                    appendNode("description", pomDesc)
                    appendNode("name", rootProject.name)
                    appendNode("url", pomUrl)
                    appendNode("licenses").appendNode("license").apply {
                        appendNode("name", pomLicenseName)
                        appendNode("url", pomLicenseUrl)
                        appendNode("distribution", pomLicenseDist)
                    }
                    appendNode("developers").appendNode("developer").apply {
                        appendNode("id", pomDeveloperId)
                        appendNode("name", pomDeveloperName)
                    }
                    appendNode("scm").apply {
                        appendNode("url", pomScmUrl)
                        appendNode("connection", pomScmConnection)
                    }
                }
            }
        }
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?


bintray {
    // Getting bintray user and key from properties file or command line
    user = findProperty("bintrayUser")
    key = findProperty("bintrayApiKey")

    // Automatic publication enabled
    publish = true
    override = true

    // Set maven publication onto bintray plugin
    setPublications("lib")

    // Configure package
    pkg.apply {
        repo = "kushetka"
        name = rootProject.name
        setLicenses("Apache-2.0")
        setLabels("Kotlin")
        vcsUrl = pomScmUrl
        websiteUrl = pomUrl
        issueTrackerUrl = pomIssueUrl
        githubRepo = githubRepo
        githubReleaseNotesFile = githubReadme

        // Configure version
        version.apply {
            name = artifactVersion
            desc = pomDesc
            released = Date().toString()
            vcsTag = project.versioning.info.tag
        }
    }
}
