pipeline {
  agent any
  def app
  
  tools {
    maven '3.8.2'
    jdk '11.0.13+8'
  }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install'
      }
    }

    stage('docker push') {
      app = docker.build("mikej091/know-bot")
    }
  }
}
