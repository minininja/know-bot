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
  - name: kubectl
    image: lachlanevenson/k8s-kubectl:v1.8.8
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
          //  sh 'mkdir -p $HOME/.m2 && echo "<settings xmlns=\"http://maven.apache.org/SETTINGS/1.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd\"><mirrors><mirror><id>central-mirror</id><name>Central Mirror</name><url>http://mini:30683/nexus/content/repositories/central/</url><mirrorOf>central</mirrorOf></mirror></mirrors></settings>" > $HOME/.m2/settings.xml'
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
  }
}
