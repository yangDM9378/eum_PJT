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
                        sshagent (credentials: ['ssh_admin']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${TARGET_HOST_FRONTEND} '
                            cd /home/S08P31C103
                            sudo git pull origin develop
                            cd frontend/
                            sudo npm install
                            sudo npm run build
                            pm2 delete 0
                            pm2 --name frontend start npm -- start
                            '
                        """
                        }
                    }
                }

                stage ('docker backend run') {
                    steps {
                        sshagent (credentials: ['ssh_admin']) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${TARGET_HOST_BACKEND} '
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
        TARGET_HOST_FRONTEND = "ubuntu@3.145.0.19"
        TARGET_HOST_BACKEND = "ubuntu@18.118.212.73"

    }
}
