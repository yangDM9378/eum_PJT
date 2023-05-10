pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage ('docker frontend run') {
            steps {
                echo 'git pull'
                git branch: 'develop', credentialsId: 'admin', url: 'https://lab.ssafy.com/s08-final/S08P31C103.git'

                echo 'frontend container, image remove'
                sh 'docker container prune -f'
                sh 'docker image prune -af'

                echo 'frontend build and up'
                sh 'docker-compose build frontend'
                sh 'docker-compose up -d frontend'
            }
        }
        stage ('docker backend run') {
            steps {
                sh 'ssh -i /var/jenkins_home/workspace/C103.pem ubuntu@3.22.167.196'
                sh 'cd /home/S08P31C103'

                echo 'git pull'
                git branch: 'develop', credentialsId: 'admin', url: 'https://lab.ssafy.com/s08-final/S08P31C103.git'

                echo 'backend container, image remove'
                sh 'sudo docker container prune -f'
                sh 'sudo docker image prune -af'

                echo 'backend build and up'
                sh 'sudo docker-compose build backend'
                sh 'sudo docker-compose up -d backend'
            }

            post {
                always {
                    sh 'exit'
                }
            }
        }
    }
}
