FROM jenkins/jenkins:lts

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

# Pour désactiver l'étape UNLOCK de la 1ère install Jenkins
ENV JAVA_OPTS=-Djenkins.install.runSetupWizard=false

# Pour préfigurer la cnx Jenkins/Sonarqube
COPY sonarqube.yml /var/jenkins_home/casc_configs/
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs    
