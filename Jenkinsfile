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
                '''

                bat '''
                    powershell -NoProfile -Command ^
                    "$started = $false; ^
                    for ($i = 1; $i -le 30; $i++) { ^
                        if (Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue) { ^
                            $started = $true; ^
                            break ^
                        }; ^
                        Start-Sleep -Seconds 2 ^
                    }; ^
                    if (-not $started) { ^
                        if (Test-Path app.log) { Get-Content app.log }; ^
                        exit 1 ^
                    }"
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
            powershell '''
                $connections = Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue
                if ($connections) {
                    $connections | ForEach-Object {
                        Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue
                    }
                }
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