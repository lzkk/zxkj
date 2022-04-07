#基础镜像
FROM hsjdk8
#维护人员信息
MAINTAINER yuhui
#设置镜像对外暴露端口
EXPOSE 5054
#将项目的jar包拷贝并命名
ADD api-gateway.jar /opt/jar/api-gateway.jar
# 设置环境变量(docker run 通过-e替换环境变量)
ENV JAVA_AGENT=""
ENV JAVA_OPTS="-server -Xms512m -Xmx512m"
ENV PROFILE="dev"
#执行启动命令
ENTRYPOINT exec java $JAVA_AGENT $JAVA_OPTS -Dspring.profiles.active=${PROFILE} -jar /opt/jar/api-gateway.jar
