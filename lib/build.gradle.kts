plugins {
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
    
    // Plugin para ejecutar la aplicación
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Lombok para reducir boilerplate
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    
    // JPA dependencies
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    
    // H2 Database
    implementation("com.h2database:h2:2.2.224")
    
    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
// Configuración para caracteres especiales (ñ, tildes)
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Configuración de la aplicación principal
application {
    mainClass.set("org.example.Main")
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}