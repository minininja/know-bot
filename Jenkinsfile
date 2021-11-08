pipeline {
  agent {
    node {
      label 'docker'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

  }
  tools {
    maven '3.8.2'
    jdk '11.0.13+8'
  }
}