import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.api.tasks.testing.Test
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("jacoco")
}

android {
    namespace = "com.example.forum"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.forum"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }
}

jacoco {
    toolVersion = "0.8.7" // Use the latest version or the version that you prefer
}

tasks.register<JacocoReport>("jacocoTestReport") {
    group = "verification"
    description = "Generate Jacoco coverage reports after running tests."

    reports {
        xml.outputLocation.set(buildDir.resolve("jacoco/testDebugUnitTestReport.xml"))
        html.outputLocation.set(buildDir.resolve("jacoco/testDebugUnitTestReport.html"))
    }

    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(fileTree("build/intermediates/javac/debug").exclude(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "android/**/*.*"
    ))


    executionData.setFrom(fileTree(mapOf(
        "dir" to "build",
        "includes" to listOf("jacoco/testDebugUnitTest.exec")
    )))
}


dependencies {
    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.2.2")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("androidx.navigation:navigation-fragment:2.7.3")
    implementation("androidx.navigation:navigation-ui:2.7.3")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    implementation("junit:junit:4.12")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics")
    // For Card view
    implementation ("androidx.cardview:cardview:1.0.0")

// Chart and graph library
    implementation ("com.github.blackfizz:eazegraph:1.2.5l@aar")
    implementation ("com.nineoldandroids:library:2.4.0")


    // Add the dependency for the Cloud Storage library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-storage")
    implementation ("com.squareup.picasso:picasso:2.71828")

}