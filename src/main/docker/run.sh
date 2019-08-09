#!/bin/sh
echo "********************************************************"
echo "Waiting for the configuration server to start on port $CONFIGSERVER_PORT"
echo "********************************************************"
while ! `nc -z configserver $CONFIGSERVER_PORT`; do sleep 3; done
echo "*******  Configuration Server has started"

echo "********************************************************"
echo "Waiting for the eureka server to start on port $EUREKASERVER_PORT"
echo "********************************************************"
while ! `nc -z discoveryserver $EUREKASERVER_PORT`; do sleep 3; done
echo "******* Eureka Server has started"

echo "********************************************************"
echo "Waiting for the ZIPKIN server to start  on port $ZIPKIN_PORT"
echo "********************************************************"
while ! `nc -z zipkin $ZIPKIN_PORT`; do sleep 10; done
echo "******* ZIPKIN has started"

echo "********************************************************"
echo "Waiting for the auth server to start on port $AUTH_PORT"
echo "********************************************************"
while ! `nc -z authserver $AUTH_PORT`; do sleep 3; done
echo "******** Database Server has started "

echo "********************************************************"
echo "Starting the Stream Server"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom                \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI            \
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI \
     -Dauth-server=$AUTH_URI                                 \
     -Dffmpeg.bin.path=$FFMPEG_BIN_PATH                       \
     -Dspring.profiles.active=$PROFILE                      \
     -Xdebug -Xnoagent -Djava.compiler=NONE                 \
     -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=$REMOTE_DEBUG_PORT \
-jar /usr/local/server/@project.build.finalName@.jar