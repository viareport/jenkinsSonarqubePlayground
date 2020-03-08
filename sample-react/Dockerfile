FROM timbru31/java-node:11

LABEL maintainer="mOuLiNeX"

WORKDIR /usr/local
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.2.0.1873-linux.zip &&\
    unzip sonar-scanner-cli-4.2.0.1873-linux.zip && \
    rm sonar-scanner-cli-4.2.0.1873-linux.zip
ENV PATH /usr/local/sonar-scanner-4.2.0.1873-linux/bin:$PATH

WORKDIR /data