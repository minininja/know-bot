pipeline {
  agent {
    kubernetes {
      yaml '''
kind: Pod
metadata:
  name: img
spec:
  containers:
  - name: img
    image: jessfraz/img
    imagePullPolicy: Always
    command:
    - cat
    tty: true
#    volumeMounts:
#      - name: docker-config
#        mountPath: /home/user/.docker
#  volumes:
#    - name: docker-config
#      configMap:
#        name: docker-config
'''
    }

  }
  stages {
    stage('Build') {
      steps {
        tool(name: '3.8.3', type: 'maven')
        tool(name: '11.0.13+8', type: 'jdk')
        sh 'mvn install -DskipTests'
      }
    }

    stage('Push') {
      steps {
        container(name: 'img') {
          sh 'img build . -t mikej091/knowbot:latest -t mikej091/knowbot:$BUILD_NUMBER'
        }

      }
    }

  }
}