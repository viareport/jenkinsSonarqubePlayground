FROM jenkins/jenkins:lts

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Pour désactiver l'étape UNLOCK de la 1ère install Jenkins
ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

# Pour préconfigurer la cnx Jenkins/Sonarqube
COPY sonarqube.yml /var/jenkins_home/casc_configs/
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs    

# Pour builds Sonar (hors Gradle)
RUN cd /var/jenkins_home &&\
	wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-4.0.0.1744-linux.zip &&\
    unzip sonar-scanner-cli-4.0.0.1744-linux.zip && \
    rm sonar-scanner-cli-4.0.0.1744-linux.zip
ENV PATH /var/jenkins_home/sonar-scanner-4.0.0.1744-linux/bin:$PATH
