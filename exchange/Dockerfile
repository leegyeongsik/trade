# exchange 서버 하위에 `Dockerfile` 생성
# 빌드 시 사용할 JDK 버전 선택 (기본 17, 필요 시 21 지정 가능)

ARG BUILD_JDK=17

# 컨테이너에 jdk 이미지 설치
FROM eclipse-temurin:${BUILD_JDK}

# 기본 포트
EXPOSE 8081

# 서버 시간대 설정
ENV TZ=Asia/Seoul

# 애플리케이션 JAR를 app.jar로 복사 (jar 파일 경로에 맞춰 조정)
# Gradle을 사용하는 경우 1 사용, Maven을 사용하는 경우 2 사용
COPY build/libs/*.jar app.jar
# 컨테이너가 실행될 때 app.jar 실행
ENTRYPOINT ["java","-jar","/app.jar"]