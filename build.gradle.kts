plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("plugin.jpa") version "1.9.25"
	kotlin("kapt") version "2.1.0"
	id("jacoco")
}

group = "com.musinsa"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

//apply(plugin = "jacoco")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
	implementation("p6spy:p6spy:3.9.1")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.4.1")

	// QueryDSL
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	implementation("com.querydsl:querydsl-apt:5.1.0:jakarta")
	implementation("com.querydsl:querydsl-sql:5.1.0")
	implementation("jakarta.persistence:jakarta.persistence-api")
	implementation("jakarta.annotation:jakarta.annotation-api")
	kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test") // Spring Boot 테스트 기본 설정
	testImplementation("org.springframework.boot:spring-boot-starter-web") // 웹 관련 설정

	// Kotest
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1") // Kotest JUnit 5 실행기
	testImplementation("io.kotest:kotest-assertions-core:5.9.1") // Kotest Assertion
	testImplementation("io.kotest:kotest-property:5.9.1") // Property-based 테스트 (선택 사항)

	// MockK
	testImplementation("io.mockk:mockk:1.13.16")

	// Spring + MockK 통합 지원
	testImplementation("com.ninja-squad:springmockk:4.0.2")

	// MyBatis 테스트 (필요한 경우)
	testImplementation("org.mybatis.spring.boot:mybatis-spring-boot-starter-test:3.0.4")

	// Kotlin Coroutines 테스트 (필요 시 추가)
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

jacoco {
	toolVersion = "0.8.10"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.withType<Test> {
	systemProperty("kotest.framework.classpath.scanning.autoscan.disable", "true")
	useJUnitPlatform()
}
