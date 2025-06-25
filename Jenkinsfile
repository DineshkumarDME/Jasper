pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'dineshdme'
        REPO_NAME = 'testing'
        IMAGE_TAG = "build-${BUILD_NUMBER}"
        DOCKER_IMAGE = "${DOCKER_HUB_USER}/${REPO_NAME}:${IMAGE_TAG}"
    }

    stages {
        stage('Clone Code') {
            steps {
                git branch: 'main', url: 'https://github.com/DineshkumarDME/Jasper.git'
            }
        }

        stage('Build Spring Boot App') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Docker Login & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Run Docker Container (Local)') {
            steps {
                script {
                    sh "docker rm -f springboot-app || true"
                    sh "docker run -d --name springboot-app -p 8080:8080 ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        success {
            echo "✅ Deployed container from image: ${DOCKER_IMAGE}"
        }
        failure {
            echo "❌ Build or deploy failed"
        }
    }
}
