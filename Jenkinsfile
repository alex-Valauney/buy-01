pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Tests Backend') {
            steps {
                echo 'Lancement des tests JUnit via Maven...'
                // La commande 'mvn test' cherche automatiquement les tests JUnit
                sh 'mvn test' 
            }
        }
    }

    post {
        // En cas d'échec des tests, on veut être prévenu !
        failure {
            mail to: 'alex.valauney01@gmail.com',
                 subject: "⚠️ ÉCHEC des tests : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Les tests ont échoué. Vérifiez les logs ici : ${env.BUILD_URL}"
        }
        success {
            mail to: 'alex.valauney01@gmail.com',
                 subject: "✅ Succès : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: "Tous les tests sont passés avec succès !"
        }
    }
}