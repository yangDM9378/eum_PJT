pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {
        stage ('git pull') {
            steps {
                echo 'git pull'
                git branch: 'develop', credentialsId: 'admin', url: 'https://lab.ssafy.com/s08-final/S08P31C103.git'
            }
        }
        stage ('verify tool') {
            steps {
                sh '''
                    docker info
                    docker version
                    docker-compose version
                '''
            }
        }

        // 사용하지 않는 container와 image를 삭제합니다.
        stage ('docker garbage collection') {
            steps {
                sh 'docker container prune -f'
                sh 'docker image prune -af'
            }
        }

        stage ('docker frontend build') {
            steps {
                sh 'cd /var/jenkins_home/workspace/eum'
                sh 'docker-compose build frontend'
            }
        }
        stage ('docker backend build') {
            steps {
                sh 'cd /var/jenkins_home/workspace/eum'
                sh 'docker-compose build backend'
            }
        }
        stage ('docker deploy') {
            steps {
                sh 'cd /var/jenkins_home/workspace/eum'

                sh 'docker-compose stop frontend && docker-compose rm -f frontend'
                sh 'docker-compose stop backend && docker-compose rm -f backend'

                sh 'docker-compose up -d frontend '
                sh 'docker-compose up -d backend '
            }
        }
    }
}
