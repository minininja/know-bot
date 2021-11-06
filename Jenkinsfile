pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'make build'
      }
    }

    stage('docker push') {
      steps {
        sh '''docker build . -t mikej091/know-bot:latest
docker push mikej091/know-bot:latest'''
      }
    }

  }
}