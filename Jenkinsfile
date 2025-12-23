pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Récupère le code depuis Git
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                echo 'Construction et tests du Backend...'
                // Remplacez par votre commande (ex: ./mvnw clean test)
                sh 'echo "Tests JUnit en cours..."' 
            }
        }

        stage('Frontend Tests') {
            steps {
                echo 'Tests du Frontend Angular...'
                // Remplacez par votre commande (ex: npm test)
                sh 'echo "Tests Jasmine en cours..."'
            }
        }
    }

    post {
        always {
            // Envoie un mail peu importe le résultat
            mail to: 'votre-email@gmail.com',
                 subject: "Statut du Build ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                 body: "Le build s'est terminé avec le statut : ${currentBuild.currentResult}\nConsultez les détails ici : ${env.BUILD_URL}"
        }
    }
}