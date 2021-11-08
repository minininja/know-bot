pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('') {
      steps {
        sh 'docker build .'
      }
    }

  }
  tools {
    maven '3.8.2'
    jdk '11.0.13+8'
  }
}