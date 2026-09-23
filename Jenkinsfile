pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        // Docker image
        DOCKER_IMAGE = 'ashishkc/hostel-management-system:latest'

        // Docker executable location on Windows
        DOCKER_PATH = 'C:\\Program Files\\Docker\\Docker\\resources\\bin'

        // Application port
        APP_PORT = '8080'
    }

    stages {

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Check Tools') {
            steps {
                bat '''
                    echo ==============================
                    echo Checking Java
                    echo ==============================
                    java -version

                    echo.
                    echo ==============================
                    echo Checking Maven
                    echo ==============================
                    mvn -version

                    echo.
                    echo ==============================
                    echo Checking Docker
                    echo ==============================
                    where docker
                    docker --version
                '''
            }
        }

        stage('Build') {
            steps {
                bat '''
                    echo ==============================
                    echo Maven Build
                    echo ==============================

                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            steps {
                bat '''
                    echo ==============================
                    echo Running Tests
                    echo ==============================

                    mvn test
                '''
            }
        }

        stage('Start Application') {
            steps {
                bat '''
                    echo ==============================
                    echo Starting Hostel Management System
                    echo ==============================

                    if exist app.log del app.log

                    start "HostelManagementSystem" /B cmd /c "java -jar target\\hostel-management-system-1.0.jar > app.log 2>&1"

                    echo Application started.
                    echo Waiting for application...

                    timeout /t 15 /nobreak
                '''
            }
        }

        stage('Docker Check') {
            steps {
                bat '''
                    echo ==============================
                    echo Docker Information
                    echo ==============================

                    docker version
                    docker info
                '''
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                    echo ==============================
                    echo Building Docker Image
                    echo ==============================

                    docker build -t %DOCKER_IMAGE% .

                    echo.
                    echo ==============================
                    echo Docker Images
                    echo ==============================

                    docker images
                '''
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    bat '''
                        echo ==============================
                        echo Docker Hub Login
                        echo ==============================

                        echo %DOCKERHUB_TOKEN% | docker login -u %DOCKERHUB_USERNAME% --password-stdin
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                bat '''
                    echo ==============================
                    echo Pushing Docker Image
                    echo ==============================

                    docker push %DOCKER_IMAGE%
                '''
            }
        }
    }

    post {

        success {
            echo '========================================'
            echo 'JENKINS PIPELINE SUCCESSFUL'
            echo '========================================'
            echo 'Docker image: ashishkc/hostel-management-system:latest'
        }

        failure {
            echo '========================================'
            echo 'JENKINS PIPELINE FAILED'
            echo '========================================'
            echo 'Check the failed stage and console output.'
        }

        always {
            bat '''
                echo.
                echo ==============================
                echo Docker Images After Pipeline
                echo ==============================

                docker images
            '''

            archiveArtifacts artifacts: 'app.log', allowEmptyArchive: true
        }
    }
}