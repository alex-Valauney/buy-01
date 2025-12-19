pipeline {
    agent any

    options {
        timeout(time: 1, unit: 'HOURS') 
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Microservices') {
            steps {
                script {
                    def services = ['discovery-service', 'gateway-service', 'user-service', 'product-service', 'media-service']
                    
                    services.each { service ->
                        stage("Build ${service}") {
                            dir("microservices/${service}") {
                                echo "Building ${service}..."
                                // Using -DskipTests for a quicker build as requested
                                sh 'chmod +x mvnw'
                                sh './mvnw clean package -DskipTests'
                            }
                        }
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo "Building Frontend..."
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline finished'
        }
        success {
            echo 'Build successful!'
        }
        failure {
            echo 'Build failed.'
        }
    }
}
