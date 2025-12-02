plugins {
    application
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Guava for Murmur3 128 hash (Bloom filter)
    implementation("com.google.guava:guava:33.0.0-jre")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // adjust if your main class has a different name/package
    mainClass.set("ch.dani.bloomfilter.Main")
}
