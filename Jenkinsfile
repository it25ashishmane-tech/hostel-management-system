pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven'
    }

    environment {
        DOCKER_PATH = 'C:\\Users\\25060\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin'
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
        bat '"C:\\Users\\25060\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" version'
    }
}

stage('Docker Build') {
    steps {
        bat 'docker build -t YOUR_DOCKERHUB_USERNAME/hostel-management-system:latest .'
    }
}

stage('Docker Push') {
    steps {
        bat 'docker push YOUR_DOCKERHUB_USERNAME/hostel-management-system:latest'
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