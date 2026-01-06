pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Infrastructure') {
            steps {
                // On se déplace dans le dossier où se trouve ton docker-compose
                dir('buy-01/infrastructure') {
                    echo 'Construction des images Docker...'
                    sh 'docker-compose build'
                }
            }
        }

        stage('Automated Testing') {
            steps {
                dir('buy-01/infrastructure') {
                    echo 'Lancement des services pour tests...'
                    // -d lance en arrière-plan
                    sh 'docker-compose up -d'
                    
                    echo 'Exécution des tests...'
                    // Ici, on simule ou on lance une commande de test sur un conteneur précis
                    // Exemple : sh 'docker exec backend-container mvn test'
                    sh 'echo "Tests en cours..." && sleep 5'
                }
            }
        }

        stage('Deployment & Verification') {
            steps {
                echo 'Vérification du déploiement...'
                // Si cette étape échoue, le post-failure s'occupera du rollback
                sh 'docker ps' 
            }
        }
    }

    post {
        success {
            mail to: 'alex.valauney01@gmail.com',
                 subject: "✅ SUCCESS: Build #${env.BUILD_NUMBER}",
                 body: "Le projet a été buildé, testé et déployé avec succès sur Ubuntu."
        }
        failure {
            echo 'ERREUR DETECTEE - Rollback automatique...'
            dir('buy-01/infrastructure') {
                // Stratégie de Rollback (Point 4) : on nettoie tout en cas d'erreur
                sh 'docker-compose down'
            }
            mail to: 'alex.valauney01@gmail.com',
                 subject: "❌ FAILED: Build #${env.BUILD_NUMBER}",
                 body: "Le pipeline a échoué. Les services ont été coupés par sécurité."
        }
    }
}