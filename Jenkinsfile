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

        stage('Build Infrastructur') {
            steps {
                // Utilisation du chemin direct vers le dossier infrastructure
                dir('infrastructure') {
                    echo 'Construction des images Docker...'
                    sh 'docker-compose build'
                }
            }
        }

        stage('Automated Testing') {
            steps {
                dir('infrastructur') {
                    echo 'Lancement des services...'
                    sh 'docker-compose up -d'
                    sh 'echo "Simulation des tests JUnit..."'
                }
            }
        }
    }

    post {
        failure {
            script {
                echo 'ERREUR - Tentative de Rollback...'
                try {
                    dir('infrastructur') {
                        sh 'docker-compose down'
                    }
                } catch (e) {
                    echo "Le rollback a échoué (normal si le build n'a pas créé d'images)."
                }
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
