pipeline {
  agent any
  environment {
    registry = "mikej091/knowbot"
    registryCredential = "dockerhub"
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }
    stage('Building image') {
      steps{
        script {
          docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
  }
  tools {
    maven '3.8.3'
    jdk '11.0.13+8'
  }
}
