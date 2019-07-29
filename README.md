# jenkinsSonarqubePlayground
Mise en place d'un environnement bac à sable (initié par Docker) avec jenkins + sonarqube

Pré-requis : docker et docker-compose

Lancement :
```
cd infra
docker-compose up -d
```

# infra

Dans ce répertoire, on trouve tout ce qu'il faut pour démarrer un serveur Jenkins (http://localhost:8080) prêt à l'emploi (plugins inclus, cf fichier [plugin.txt](https://github.com/viareport/jenkinsSonarqubePlayground/blob/master/infra/jenkins/plugins.txt)) branché sur un serveur Sonarqube (http://localhost:9000) lui aussi prết à l'emploi (données persistés dans base PostgreSQL et donc résistantes au redémarrage).

*Attention* : Pour le moment le conteneur Jenkins ne peut pas (pour le moment) faire du "Docker in Docker".

# sample

Projet "sample" (Kotlin) pouvant être analysé dans Sonarqube (violation + coverage) via build Jenkins/Gradle.

# Comment déclarer des jobs du projet 'sample' dans Jenkins ?

Le contenu du projet est visible dans le conteneur Jenkins dans le répertoire */opt/app/sample* et il existe une entrée "File System" (merci au plugin qui va bien) au niveau du Source Code Management de Jenkins