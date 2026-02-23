pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                // On liste les fichiers pour être SÛR du chemin dans la console
                sh 'ls -R' 
            }
        }

        stage('Code Quality') {
            steps {
                script {
                    def services = ['discovery-service', 'gateway-service', 'user-service', 'product-service', 'media-service']
                    services.each { service ->
                        dir("microservices/${service}") {
                            echo "🔍 Analyse SonarQube pour ${service}..."
                            // Attention : Si Jenkins est dans Docker, localhost pointe vers le conteneur.
                            // Il faudra peut-être utiliser host.docker.internal ou l'IP de la machine.
                            // On tente avec la config par défaut pour l'instant.
                            try {
                                // On passe le token SonarQube et l'URL pointant vers la machine hôte (172.17.0.1 sur Linux)
                                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                                    sh './mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.host.url=http://172.17.0.1:9000 -Dsonar.login=$SONAR_TOKEN -Dsonar.projectKey=test-pour-safe-zone-${service} -Dsonar.projectName="test pour safe zone - ${service}"'
                                }
                            } catch (e) {
                                echo "⚠️ Attention: L'analyse Sonar a échoué pour ${service} (Sonar est-il allumé sur le port 9000 ? IP Docker : 172.17.0.1)"
                                // On ne fail pas le build pour ça si c'est juste un test local
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        failure {
            script {
                echo 'ERREUR - Le pipeline a échoué.'
            }
            // Envoi du mail d'échec
            mail to: 'alex.valauney01@gmail.com',
                 subject: "❌ ÉCHEC Build #${env.BUILD_NUMBER}",
                 body: "Le build a échoué. Regarde ici : ${env.BUILD_URL}"
        }
        success {
            mail to: 'alex.valauney01@gmail.com',
                 subject: "✅ SUCCÈS Build #${env.BUILD_NUMBER}",
                 body: "Le déploiement est réussi !"
        }
    }
}
