pipeline {
    agent any

    stages {
        stage('Initialisation') {
            steps {
                echo 'Début du pipeline pour le projet e-commerce...'
            }
        }
        stage('Simulation Build') {
            steps {
                echo 'En train de compiler le code...'
                // On simule une action de build
                sh 'echo "Compilation terminée"'
            }
        }
    }

    post {
        always {
            echo 'Envoi de la notification mail...'
            mail to: 'alex.valauney01@gmail.com',
                 subject: "Statut Jenkins : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """Bonjour,

Le build numéro ${env.BUILD_NUMBER} de votre projet ${env.JOB_NAME} est terminé.

Statut : ${currentBuild.currentResult}
Lien vers le build : ${env.BUILD_URL}

--
Votre serveur Jenkins (Ubuntu 24.04)"""
        }
    }
}
