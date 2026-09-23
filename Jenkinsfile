pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
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
                bat 'timeout /t 10 /nobreak'
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t ashishkc/hostel-management-system:latest .'
            }
        }

        stage('Docker Push') {
            steps {

                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {

                    bat 'echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin'

                    bat 'docker push ashishkc/hostel-management-system:latest'
                }
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