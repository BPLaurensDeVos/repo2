pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'jdk17'
    }

    environment {
        GITHUB_ACTOR = credentials('GITHUB_ACTOR')
        GITHUB_TOKEN = credentials('PAT_TOKEN')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        REPO1_URL = "https://maven.pkg.github.com/BPLaurensDeVos/repo1/com/example/repo1/repo1/1.0/repo1-1.0.jar"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/BPLaurensDeVos/repo2.git'
            }
        }

        stage('Artifact Check & Trigger') {
            steps {
                script {
                    def statusCode = sh(
                        script: "curl -L -u ${GITHUB_ACTOR}:${GITHUB_TOKEN} -s -o /dev/null -w '%{http_code}' ${REPO1_URL}",
                        returnStdout: true
                    ).trim()

                    if (statusCode != '200') {
                        echo "Artifact missing. Triggering Repo1 Jenkins pipeline..."
                        build job: 'repo1', wait: true, propagate: true
                    } else {
                        echo "Artifact is available. Proceeding with build."
                    }
                }
            }
        }

        stage('Configure Maven') {
            steps {
                sh """
                mkdir -p ~/.m2
                echo "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0'
                      xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                      xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'>
                    <servers>
                        <server>
                            <id>github</id>
                            <username>${GITHUB_ACTOR}</username>
                            <password>${GITHUB_TOKEN}</password>
                        </server>
                    </servers>
                  </settings>" > ~/.m2/settings.xml
                """
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('SonarCloud Scan') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }


        stage('Deploy to GitHub Packages') {
            steps {
                sh "mvn deploy"
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
