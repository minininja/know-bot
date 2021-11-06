pipeline {
  agent any
  stages {
    stage('Tools') {
      steps {
        sh 'make build'
        tool(type: 'go', name: '1.17.2')
      }
    }

    stage('Build') {
      steps {
        sh 'make build'
      }
    }

    stage('Docker Push') {
      steps {
        sh '''docker build . -t mikej091/know-bot:latest
docker push mikej091/know-bot:latest'''
      }
    }

  }
}