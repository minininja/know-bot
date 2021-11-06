pipeline {
  agent {
    kubernetes {
      containerTemplate {
        name 'cmake'
        image 'mikej091/inbound-agent:4.11-1'
        command 'sleep'
        args '999'
      }

    }

  }
  stages {
    stage('Tools') {
      steps {
        tool(type: 'go', name: '1.17.2')
      }
    }

    stage('Build') {
      steps {
        sh '''go get -u github.com/Necroforger/dgrouter/exrouter
go get -u github.com/bwmarrin/discordgo
go build'''
      }
    }

    stage('Docker Push') {
      steps {
        sh '''docker build . -t mikej091/know-bot:latest
docker push mikej091/know-bot:latest'''
      }
    }

  }
  environment {
    BINARY = 'know-bot'
    PATH = '$PATH:/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin'
  }
}