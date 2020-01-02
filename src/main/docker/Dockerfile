ARG OPENSSL_VERSION=1.0.2u
ARG PCRE_VERSION=8.42
ARG ZLIB_VERSION=1.2.11
ARG NGINX_RTMP_VERSION=1.2.1
ARG NGINX_VERSION=1.15.3
##############DOWNLOAD SOURCE######################
FROM alpine:3.8 as build-source
ARG OPENSSL_VERSION
ARG PCRE_VERSION
ARG ZLIB_VERSION
ARG NGINX_RTMP_VERSION
ARG NGINX_VERSION
RUN cd /tmp/\
    && wget https://www.openssl.org/source/openssl-${OPENSSL_VERSION}.tar.gz\
	&& tar -xzvf openssl-${OPENSSL_VERSION}.tar.gz\
	&& rm openssl-${OPENSSL_VERSION}.tar.gz
RUN	cd /tmp/\
    && wget https://ftp.pcre.org/pub/pcre/pcre-${PCRE_VERSION}.tar.gz\
	&& tar -xzvf pcre-${PCRE_VERSION}.tar.gz\
	&& rm pcre-${PCRE_VERSION}.tar.gz
RUN cd /tmp/\
    && wget http://www.zlib.net/zlib-${ZLIB_VERSION}.tar.xz\
	&& tar -xvf zlib-${ZLIB_VERSION}.tar.xz\
	&& rm zlib-${ZLIB_VERSION}.tar.xz
RUN cd /tmp/\
    && wget https://github.com/arut/nginx-rtmp-module/archive/v${NGINX_RTMP_VERSION}.tar.gz\
	&& tar -xzvf v${NGINX_RTMP_VERSION}.tar.gz\
	&& rm v${NGINX_RTMP_VERSION}.tar.gz
RUN cd /tmp/\
    && wget http://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz\
	&& tar xzvf nginx-${NGINX_VERSION}.tar.gz\
	&& rm nginx-${NGINX_VERSION}.tar.gz
###########################
FROM ray0728/ffmpeg:1.0
ARG OPENSSL_VERSION
ARG PCRE_VERSION
ARG ZLIB_VERSION
ARG NGINX_RTMP_VERSION
ARG NGINX_VERSION
ADD sources.list /etc/apt/
RUN apt update && apt install -y netcat-openbsd build-essential make
RUN mkdir -p /usr/local/server
RUN mkdir -p /mnt/source/
COPY --from=build-source /tmp/openssl-${OPENSSL_VERSION} /mnt/source/
COPY --from=build-source /tmp/pcre-${PCRE_VERSION} /mnt/source/
COPY --from=build-source /tmp/zlib-${ZLIB_VERSION} /mnt/source/
COPY --from=build-source /tmp/nginx-rtmp-module-${NGINX_RTMP_VERSION} /mnt/source/
COPY --from=build-source /tmp/nginx-${NGINX_VERSION} /mnt/source/nginx
RUN cd /mnt/source/nginx && ./configure --prefix=/usr/local/nginx-${NGINX_VERSION} --with-openssl=/mnt/source/openssl-{OPENSSL_VERSION}/ --with-pcre=/mnt/source/pcre-{PCRE_VERSION}/ \ --with-zlib=/mnt/source/zlib-{ZLIB_VERSION}/ --add-module=/mnt/source/nginx-rtmp-module-${NGINX_RTMP_VERSION}/ --with-http_ssl_module && make && make install
RUN cd /&&rm -rf /mnt/
ADD @project.build.finalName@.jar /usr/local/server
ADD nginx.conf /etc/nginx/nginx.conf
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 1935
CMD ./run.sh
