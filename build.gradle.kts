plugins {
	`java-library`
}

repositories {
	jcenter()
}

dependencies {
	compileOnly("org.jetbrains:annotations:17.0.0")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.2")
	testCompileOnly("org.jetbrains:annotations:17.0.0")
}

java {
	sourceCompatibility = JavaVersion.VERSION_11
	targetCompatibility = JavaVersion.VERSION_11
}

tasks {
	test {
		useJUnitPlatform {
			includeEngines("junit-jupiter")
		}
	}
}
