pipeline {
  agent any
  stages {
    stage('Tool Install') {
      steps {
        tool(name: '1.17.2', type: 'go')
        tool(name: '3.21.4', type: 'cmake')
      }
    }

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