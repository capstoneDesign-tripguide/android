plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.secondcapstone"
    compileSdk = 33 //디폴트는 34

    defaultConfig {
        applicationId = "com.example.secondcapstone"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    //뷰 바인딩 허용
    buildFeatures{
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0") //디폴트는 1.10.0인데 내 폰이랑 호환성 문제로 1.9.0으로 낮춤
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.1.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0") //retrofit을 위한 의존성1
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") //retrofit을 위한 의존성2

    //네아로 모듈을 위한 의존성들 사용(Project/main/libs)
    //implementation(files("libs/oauth-5.9.0.aar"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.legacy:legacy-support-core-utils:1.0.0")
    implementation("androidx.browser:browser:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
    implementation("com.airbnb.android:lottie:3.1.0")
    implementation ("com.navercorp.nid:oauth:5.9.0") // 네이버로그인 jdk 11용
    //implementation("com.navercorp.nid:oauth-jdk8:5.9.0") // jdk 8


    implementation("com.kakao.sdk:v2-all:2.19.0") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.19.0") // 카카오 로그인
    implementation("com.kakao.sdk:v2-talk:2.19.0") // 친구, 메시지(카카오톡)
    implementation("com.kakao.sdk:v2-share:2.19.0") // 메시지(카카오톡 공유)
    implementation("com.kakao.sdk:v2-friend:2.19.0") // 카카오톡 소셜 피커, 리소스 번들 파일 포함
    implementation("com.kakao.sdk:v2-navi:2.19.0") // 카카오내비
    implementation("com.kakao.sdk:v2-cert:2.19.0") // 카카오 인증서비스

    implementation("androidx.drawerlayout:drawerlayout:1.1.1") //드로어 레이아웃

    implementation("com.google.android.gms:play-services-maps:17.0.0") //구글맵
    implementation("com.google.android.gms:play-services-location:17.0.0") //구글맵2

    implementation("com.android.volley:volley:1.2.0") //구글 맵 API에 경로 그리기
    implementation("com.google.maps.android:android-maps-utils:2.2.5")
}
