![travis_ci](https://www.travis-ci.org/ray0728/streamserver.svg?branch=master)
# streamserver
## 说明
基于FFMpeg的流服务器，提供基于FFMpeg的HLS切片服务。
* 服务对外提供一个RESTFUL接口，允许对指定资源做HLS切片，并将结果发送给[
MessageServer][1]。

## ToDo
* 无权限验证
* 无文件合法性验证
* 无推流服务

## 运行方式：  
application.properties中并**不包含**完整配置信息，所以**不支持**直接运行  
* java 方式

```java
java
-Djava.security.egd=file:/dev/./urandom                  \
-Dspring.cloud.config.uri=$CONFIGSERVER_URI              \
-Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
-Dauth-server=$AUTH_URI                                  \
-Dffmpeg.bin.path=$FFMPEG_BIN_PATH                       \
-Dspring.profiles.active=$PROFILE                        \
-jar target.jar
```
* docker 方式  
建议用docker-compose方式运行

```docker
streamserver:
    image: ray0728/streamserv:1.0
    entrypoint: /bin/sh
    command:
      - -c
      - ./run.sh
    ports:
      - "10008:10008"
    environment:
      CONFIGSERVER_PORT: "10002"
      EUREKASERVER_PORT: "10001"
      ZIPKIN_PORT:  "9411"
      AUTH_PORT: authserver端口
      CONFIGSERVER_URI: 配置服务地址
      EUREKASERVER_URI: EUREKA地址
      AUTH_URI: authserver地址
      FFMPEG_BIN_PATH: FFMPEG执行文件目录
      PROFILE: "dev"
    volumes:
      - /home/core/resource:/mnt/resource
```  
关于Docker  
编译完成的Docker位于[Dockerhub][2]请结合Release中的[Tag标签][3]获取对应的Docker

[1]:https://github.com/ray0728/messageserver
[2]:https://hub.docker.com/r/ray0728/streamserv/tags
[3]:https://github.com/ray0728/streamserver/tags
