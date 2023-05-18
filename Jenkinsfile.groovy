pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage ("deploy") {
            parallel {
                stage ('docker frontend run') {
                    steps {
                        echo 'git pull'
                        git branch: 'develop', credentialsId: 'admin', url: 'https://lab.ssafy.com/s08-final/S08P31C103.git'

                        echo 'frontend container, image remove'
                        sh 'pwd'
                        sh 'docker container prune -f'
                        sh 'docker image prune -af'
                        echo 'frontend build and up'
                        sh 'docker-compose up --build -d frontend'
                    }
                }

                stage ('docker backend run') {
                    steps {
                        sshagent (credentials: ['ssh_admin']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${TARGET_HOST} '
                            cd /home/S08P31C103/
                            sudo git pull origin develop
                            sudo docker container prune -f
                            sudo docker image prune -af
                            sudo docker-compose build backend
                            sudo docker-compose up -d backend
                            '
                        """
                        }
                    }
                }

            }
        }
    }

    environment {
        TARGET_HOST = "ubuntu@18.118.212.73"

    }
}
