# MavenとJDKをベースとするDockerイメージを選択
FROM maven:3.8.3-openjdk-11 AS build

# コンテナ内の作業ディレクトリを指定
WORKDIR /usr/src/app

# Mavenの依存関係をキャッシュする
COPY pom.xml .
RUN mvn dependency:go-offline

# アプリケーションのソースコードをコピー
COPY src ./src

# アプリケーションをビルド
RUN mvn package -DskipTests

# 本番用の軽量なJREをベースとするDockerイメージを選択
FROM adoptopenjdk/openjdk11:alpine-jre AS runtime
# ビルドステージからビルド済みのJARファイルをコピー
COPY --from=build /usr/src/app/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

# ポートのエクスポート
EXPOSE 8080

# アプリケーションを実行
CMD ["java", "-jar", "/app/demo.jar"]