# 1단계: 빌드 단계
FROM gradle:8.6-jdk17 AS build


# 작업 디렉토리 설정
WORKDIR /app

# Gradle 캐시를 활용하기 위해 Gradle Wrapper 및 소스 파일 복사
COPY ../gradlew .
COPY ../gradle gradle/
COPY ../build.gradle .
COPY ../settings.gradle .

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew  # 실행 권한 부여

# Gradle 캐시를 활용하여 의존성 설치
RUN ./gradlew build -x test --stacktrace --no-daemon || return 0

# 프로젝트 소스 복사
COPY survey-config-server/ survey-config-server/

# 필요한 모듈 빌드
RUN ./gradlew :survey-config-server:build -x test --stacktrace --no-daemon

# 2단계: 실행 단계
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물 복사
COPY --from=build /app/survey-config-server/build/libs/*.jar app.jar

# 컨테이너 실행 시 애플리케이션 실행
ENV SPRING_PROFILE "local"

# ARG로 값 받기
ARG CONFIG_USERNAME
ARG CONFIG_PASSWORD
# ENV로 환경 변수 설정
ENV CONFIG_USERNAME=$CONFIG_USERNAME
ENV CONFIG_PASSWORD=$CONFIG_PASSWORD

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "app.jar"]
