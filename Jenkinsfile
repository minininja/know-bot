pipeline {
  agent {
    kubernetes {
      yaml '''
kind: Pod
metadata:
  name: img
spec:
  containers:
  - name: maven
    image: adoptopenjdk/maven-openjdk11
    command:
    - cat
    tty: true
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - sleep
    args:
    - 9999999
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /kaniko/.docker
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: docker-credentials 
          items:
            - key: .dockerconfigjson
              path: config.json
'''
    }

  }
  stages {
    stage('Build') {
      steps {
        container(name: 'maven') {
          sh 'mvn install -DskipTests'
        }

      }
    }

    stage('Push') {
      steps {
        container(name: 'kaniko') {
          sh 'ls target'
          sh '/kaniko/executor --context `pwd` -c `pwd` --destination=mikej091/knowbot:latest --destination=mikej091/knowbot:$BUILD_NUMBER'
        }

      }
    }

    stage('Deploy') {
      steps {
        sh 'kubectl apply -f deploy.yaml -n kbbot'
      }
    }

  }
}