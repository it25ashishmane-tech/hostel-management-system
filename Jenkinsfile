pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin'
        DOCKER_IMAGE = 'ashishkc/hostel-management-system:latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/it25ashishmane-tech/hostel-management-system.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Start Application') {
            steps {
                bat 'start /B cmd /c "mvn exec:java > app.log 2>&1"'
                bat 'powershell -Command "Start-Sleep -Seconds 10"'
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Check') {
            steps {
                bat '"%DOCKER_PATH%\\docker.exe" version'
            }
        }

        stage('Docker Build') {
            steps {
                bat '"%DOCKER_PATH%\\docker.exe" build -t %DOCKER_IMAGE% .'
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    bat '''
                        echo %DOCKER_PASSWORD% | "%DOCKER_PATH%\\docker.exe" login -u "%DOCKER_USERNAME%" --password-stdin
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                bat '"%DOCKER_PATH%\\docker.exe" push %DOCKER_IMAGE%'
            }
        }
    }

    post {

        always {
            powershell '''
                $p = (Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue).OwningProcess
                if ($p) {
                    Stop-Process -Id $p -Force
                }
            '''
        }

        success {
            echo 'Hostel Management System pipeline completed successfully!'
        }

        failure {
            echo 'Hostel Management System pipeline failed!'
        }
    }
}