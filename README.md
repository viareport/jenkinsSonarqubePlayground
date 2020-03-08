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

*Notez bien* : Le conteneur Jenkins peut maintenant faire du "Docker in Docker" :+1

## Complément de conf Sonarqube

Rajouter le Webhook suivant (http://localhost:9000/admin/webhooks) : http://jenkins:8080/sonarqube-webhook/	
Créer un token pour l'utilisateur admin/admin

## Complément de conf Jenkins

Compléter la déclaration du serveur Sonarqube avec le token créé précédent (attention, il faut le créer en tant que "Secret Text" via le 'Jenkins Credentials Provider')

### Liste des plugins (plugins.txt)

Pour obtenir la liste (+ les version) des plugins installés dans Jenkins, utilisez la commande suivante (avec éventuellement une redirection pour conserver ces infos dans un fichier) :
```
curl -sSL "http://localhost:8080/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins" | perl -pe 's/.*?<shortName>([\w-]+).*?<version>([^<]+)()(<\/\w+>)+/\1 \2\n/g'|sed 's/ /:/'
```

# sample

Projet "sample" (Kotlin mono module buildé par Gradle) pouvant être analysé dans Sonarqube (violation + coverage) via build Jenkins/Gradle.

## Création du pipeline de job

Avec le script suivant :
* En mode 'scripted pipeline' :
```
node {
   stage 'checkout'
   checkout filesystem(clearWorkspace: false, copyHidden: false, path: '/opt/app/sampleKt')
   
   stage 'Test'
   sh './gradlew clean test --stacktrace'
   
   step([$class: 'JUnitResultArchiver', testResults: 'build/test-results/test/*.xml'])
   
   stage 'Coverage'
   sh './gradlew jacocoTestReport --stacktrace'
   
   stage("Sonarqube analysis") {
       withSonarQubeEnv("Local") {
        sh './gradlew sonarqube -Dsonar.projectVersion=Build$BUILD_ID --stacktrace'
        }
   }
   
   stage("Sonarqube Quality Gate"){
      timeout(time: 2, unit: 'MINUTES') {
          def qg = waitForQualityGate()
          if (qg.status != 'OK') {
              error "Pipeline aborted due to quality gate failure: ${qg.status}"
          }
      }
  }      
}
```

* En mode 'declarative pipeline' :
```
pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
          checkout filesystem(clearWorkspace: false, copyHidden: false, path: '/opt/app/sampleKt')
        }
      }
     
      stage('Test') {
        steps {
          sh './gradlew clean test --stacktrace'
       
          junit(allowEmptyResults: true, testResults: '**/build/test-results/test/*.xml')
        }
      }
     
      stage('Coverage') {
        steps {
          sh './gradlew jacocoTestReport --stacktrace'    
        }
      }
     
     
      stage('Sonarqube analysis') {
        steps {
            withSonarQubeEnv("Local") {
              sh './gradlew sonarqube -Dsonar.projectVersion=Build$BUILD_ID --stacktrace'
            }
         }
     }
     
      stage('Sonarqube Quality Gate'){
        steps {
            timeout(time: 2, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
              }
          }
      }      
  }
}
```

# sample-multi

Projet "sample-multi" (Kotlin multi module buildé par Gradle avec dissémination des tests unitaires sur l'ensemble des modules) pouvant être analysé dans Sonarqube (violation + coverage) via build Jenkins/Gradle.

## Création des pipelines des jobs

![image](https://user-images.githubusercontent.com/1741846/63347413-552b8f80-c357-11e9-8e84-043055b0c413.png)

4 pipelines sont à créer :
- `sample-multi-pipeline` basé sur le fichier *Jenkinsfile* à la racine : ce job initialise la chaîne de build complête en déclenchant les tests unitaires et le calcul de la couverture de code globale (= fusion des couvertures de chaque sous-module) + déclenchement des downstream jobs
- `sample-multi-commons-sonar` basé sur le fichier *commons/Jenkinsfile* : ce job lance l'analyse Sonarqube sur le module `commons` (partagé par les modules principaux) + déclenchement des downstream jobs
- `sample-multi-main1-sonar` basé sur le fichier *main1/Jenkinsfile* : ce job lance l'analyse Sonarqube sur le module `main1` 
- `sample-multi-main2-sonar` basé sur le fichier *main2/Jenkinsfile* : ce job lance l'analyse Sonarqube sur le module `main2` 

Le principe est donc de lancer les analyses Sonarqube sous forme de downstreams jobs (1 job par analyse pour pouvoir mette le build en échec si pb sur QualityGate) dans l'ordre inverse des dépendances Gradle. Pour avoir des temps de build, on exploite le rapport de couverture de test du 1er pipeline qui est ensuite transmis (utilisation pugin *copyArtifact*) aux jobs downstreams.

# sample-react

Projet "sample" (JS/React initié par Create React App) pouvant être analysé dans Sonarqube (violation + coverage) via build Jenkins/sonar-scanner.

## Création du pipeline de job

Avec le script suivant :
* En mode 'declarative pipeline' :
```
pipeline {
  agent none
  stages {
    stage('checkout') {
        agent any
      steps {
          checkout filesystem(clearWorkspace: false, copyHidden: false, path: '/opt/app/sampleReact')
        }
      }
     
      stage('Inside Docker') {
          agent { 
              dockerfile {
                  args '--tmpfs /.npm --network=infra_mynetwork'        
              }
          }
          stages {
              stage('Build') {
                  steps {
                    script {
                          sh 'npm install'
                      }
                  }
              }
              stage('Coverage') {
                  steps {
                    script {
                          sh 'npm run coverage'
                      }
                  }
              }
              stage('Sonarqube analysis') {
                    steps {
                        withSonarQubeEnv("Local") {
                          sh 'sonar-scanner -Dsonar.projectVersion=Build$BUILD_ID'
                        }
                    }
                }
                 
                stage('Sonarqube Quality Gate'){
                    steps {
                        timeout(time: 2, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }      
          }
      }
  }
}
```

# Comment déclarer des jobs sur des projets locaux (hors Git) dans Jenkins ?

Le contenu du projet est visible dans le conteneur Jenkins dans le répertoire */opt/app/sample* et il existe une entrée "File System" (merci au plugin qui substitue le filesystem à un SCM) au niveau du Source Code Management de Jenkins



