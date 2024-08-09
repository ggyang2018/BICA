plugins {
    id("java")
    id("application")
}

group = "tech.sunnykit"
//version = "2"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jfree:jfreechart:1.5.4")
    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.apache.pdfbox:pdfbox:2.0.30")
    implementation("tech.sunnykit:guikit:1")
    // https://mvnrepository.com/artifact/org.hsqldb/hsqldb
    implementation("org.hsqldb:hsqldb:2.7.2")

}

application {
    // Define the main class for the application.
    mainClass.value("parser.Runner")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "parser.Runner"
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


tasks.test {
    useJUnitPlatform()
}