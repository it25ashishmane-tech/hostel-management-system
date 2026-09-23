pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
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
                bat '''
                    if exist app.log del /f /q app.log

                    start "HostelManagementSystem" /B cmd /c "java -jar target\\hostel-management-system-1.0.jar > app.log 2>&1"

                    for /L %%i in (1,1,30) do (
                        netstat -ano | findstr ":8081" >nul
                        if not errorlevel 1 exit /b 0
                        timeout /t 2 /nobreak >nul
                    )

                    type app.log
                    exit /b 1
                '''
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE% .'
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
                    bat 'docker push %DOCKER_IMAGE%'
                }
            }
        }
    }

    post {
        always {
            bat '''
                for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081"') do taskkill /F /PID %%a >nul 2>&1
            '''
        }

        success {
            echo 'Pipeline completed successfully.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}