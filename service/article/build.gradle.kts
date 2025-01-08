plugins {
    kotlin("plugin.jpa") version "1.9.25"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation(project(":common:snowflake"))
    implementation(project(":common:rest-exception-handler"))
    implementation(project(":common:aop-layer-logger"))
}
