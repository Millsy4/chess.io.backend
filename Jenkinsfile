pipeline {
    agent any
  //triggers {pollSCM('* * * * *')}
  stages {
    stage('Checkout') {
      steps {
        // Get some code from a GitHub repository
        git branch: "main", url: 'https://github.com/Millsy4/chess.io.backend.git'
      }
    }
        stage('Build') {
      steps {
        sh 'chmod a+x mvnw'
        sh './mvnw clean package -DskipTests=true'
      }

          post {
        always {
          archiveArtifacts 'target/*.jar'
        }
          }
        }
        stage('DockerBuild') {
      steps {
        sh 'docker build -t millsy4/chess.io.backend:latest .'
      }
        }
         stage('DockerRun') {
      steps {
        sh 'docker run -p 9090:9090 millsy4/chess.io.backend'
      }
        }
  }
}
