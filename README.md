# jenkinsSonarqubePlayground
Mise en place d'un environnement bac à sable (initié par Docker) avec jenkins + sonarqube

Pré-requis : docker et docker-compose

Lancement :
```
docker-compose up -d
```
Cela démarre un serveur Jenkins (http://localhost:8080) prêt à l'emploi (plugins inclus, cf fichier plugin.txt) qui est branché sur un serveur Sonarqube (http://localhost:9000) lui aussi prết à l'emploi (sur base PostgreSQL).

On propose également un project "sample" (Kotlin) pouvant être analysé dans Sonarqube via build Jenkins.

