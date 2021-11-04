pipeline {
  agent any
  stages {
    stage('Tool Install') {
      steps {
        tool(name: 'go', type: '1.17.2')
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