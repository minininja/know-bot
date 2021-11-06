pipeline {
  agent any
  stages {
    stage('Tool Install') {
      steps {
        tool(type: 'jdk', name: '11.0.13+8')
        tool(name: '3.8.2', type: 'Maven')
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean install'
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