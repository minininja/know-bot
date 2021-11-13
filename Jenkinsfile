pipeline {
  agent any
  environment {
    registry = "mikej091/knowbot"
    registryCredential = "dockerhub"
  }
  stages {
    stage('Build App') {
      steps {
        container('jnlp') {
          sh 'mvn clean install'
        }
      }
    }
    stage('Build image') {
      steps{
        container('dind-daemon') {
          script {
            docker.build registry + ":$BUILD_NUMBER"
          }
        }
      }
    }
  }
  tools {
    maven '3.8.3'
    jdk '11.0.13+8'
  }
}
