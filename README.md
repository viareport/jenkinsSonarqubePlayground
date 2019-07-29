# jenkinsSonarqubePlayground
Mise en place d'un environnement bac à sable (initié par Docker) avec jenkins + sonarqube

Pré-requis : docker et docker-compose

Lancement :
```
docker-compose up -d
```

# infra

Dans ce répertoire, on trouve tout ce qu'il faut pour démarrer un serveur Jenkins (http://localhost:8080) prêt à l'emploi (plugins inclus, cf fichier [plugin.txt](https://github.com/viareport/jenkinsSonarqubePlayground/blob/master/infra/jenkins/plugins.txt)) branché sur un serveur Sonarqube (http://localhost:9000) lui aussi prết à l'emploi (données persistés dans base PostgreSQL et donc résistantes au redémarrage).

# sample

Projet "sample" (Kotlin) pouvant être analysé dans Sonarqube (violation + coverage) via build Jenkins/Gradle.

