pipeline {
  agent {
    kubernetes {
      yaml """
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
"""  
    }
  }
  stages {
    stage('Build') {
      steps {
        sh "mvn install -DskipTests"
      }
    }
    stage('Push') {
      steps {
        container('img') {
            sh 'img build . -t mikej091/knowbot:latest -t mikej091/knowbot:$BUILD_NUMBER'
        }
      }
    }
  }
}
