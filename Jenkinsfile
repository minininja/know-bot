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
        sh '''
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go get golang.org/x/tools/cmd/goimports
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go get github.com/golang/lint/golint
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go get github.com/onsi/ginkgo/ginkgo
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go get github.com/alecthomas/gometalinter
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go get github.com/golang/dep/cmd/dep
dep ensure
/home/jenkins/agent/tools/org.jenkinsci.plugins.golang.GolangInstallation/1.17.2/bin/go build -tags \'${BINARY}\' $(GOFLAGS) -o ${BINARY} -ldflags $(VERSION_STR)'''
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
  }
}