pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

  }
  tools {
    maven '3.8.3'
    jdk '11.0.13+8'
  }
}