FROM timbru31/java-node:8

MAINTAINER mOuLiNeX

WORKDIR /usr/local
RUN wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.0.0.1744-linux.zip &&\
    unzip sonar-scanner-cli-4.0.0.1744-linux.zip && \
    rm sonar-scanner-cli-4.0.0.1744-linux.zip
ENV PATH /usr/local/sonar-scanner-4.0.0.1744-linux/bin:$PATH

WORKDIR /data