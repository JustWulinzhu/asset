# 基础镜像：选择官方的Java 8/11镜像（根据你的SpringBoot项目JDK版本选）
FROM eclipse-temurin:17-jre-alpine

#构建者
MAINTAINER wulinzhu

# 设置工作目录
WORKDIR /app

# 把本地的Jar包复制到容器的/app目录下（左边是本地Jar包名，右边是容器内名称，建议一致）
COPY asset-0.0.1-SNAPSHOT.jar /app/asset-0.0.1-SNAPSHOT.jar

# 暴露SpringBoot项目的端口（必须和你项目的server.port一致，比如8080）
EXPOSE 8080

# 容器启动时执行的命令：运行Jar包
ENTRYPOINT ["java","-jar","/app/asset-0.0.1-SNAPSHOT.jar"]