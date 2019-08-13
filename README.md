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

## Complément de conf Sonarqube

Rajouter le Webhook suivant (http://localhost:9000/admin/webhooks) : http://jenkins:8080/sonarqube-webhook/	
Créer un token pour l'utilisateur admin/admin

## Complément de conf Jenkins

Compléter la déclaration du serveur Sonarqube avec le token créé précédent (attention, il faut le créer en tant que "Secret Text" via le 'Jenkins Credentials Provider')

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



