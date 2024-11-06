plugins {
    kotlin("jvm") version "1.9.21"
}

group = "jp.hyrrot.terraform_tests"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.json:json:20240303")
    testImplementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    testImplementation("com.google.cloud:google-cloud-bigquerydatatransfer:2.52.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
    implementation("com.google.cloud:google-cloud-bigquerydatatransfer:2.52.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}