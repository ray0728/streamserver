FROM ray0728/ffmpeg:1.0
ADD sources.list /etc/apt/
RUN apt update && apt install -y netcat-openbsd build-essential make
RUN mkdir -p /usr/local/server
RUN mkdir -p /mnt/source/
RUN cd /mnt/source/
RUN wget https://www.openssl.org/source/openssl-1.1.1.tar.gz && tar -xzvf openssl-1.1.1.tar.gz
RUN wget https://ftp.pcre.org/pub/pcre/pcre-8.42.tar.gz && tar -xzvf pcre-8.42.tar.gz
RUN wget http://www.zlib.net/zlib-1.2.11.tar.xz && tar -xzvf zlib-1.2.11.tar.xz
RUN wget https://github.com/arut/nginx-rtmp-module/archive/v1.2.1.tar.gz && tar -xzvf nginx-rtmp-module-1.2.1.tar.gz
RUN wget http://nginx.org/download/nginx-1.15.3.tar.gz && tar xzvf nginx-1.15.3.tar.gz && cd nginx-1.15.3
RUN ./configure --prefix=/usr/local/nginx-1.15.3 --with-openssl=/mnt/source/openssl-1.1.1/ --with-pcre=/mnt/source/pcre-8.42/ \ --with-zlib=/mnt/source/zlib-1.2.11/ --add-module=/mnt/source/nginx-rtmp-module-1.2.1/ --with-http_ssl_module && make && make install
RUN cd /&&rm -rf /mnt/
ADD @project.build.finalName@.jar /usr/local/server
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh
