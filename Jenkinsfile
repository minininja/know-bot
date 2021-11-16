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
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: jenkins-docker-cfg
        mountPath: /root
  volumes:
  - name: jenkins-docker-cfg
    projected:
      sources:
      - secret:
          name: secret-dockercfg
          items:
            - key: configjson
              path: .docker/config.json
# sh 'img build . -t mikej091/knowbot:latest -t mikej091/knowbot:$BUILD_NUMBER'
# sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure-skip-tls-verify --destination=mikej091/knowbot'
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
          sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --destination=mikej091/knowbot'
        }
      }
    }
  }
}
