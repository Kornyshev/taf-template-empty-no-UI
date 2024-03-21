pipeline {
    agent any

    parameters {
        string(name: 'TAF_BRANCH', defaultValue: 'master', description: 'Branch of Test Automation Framework', trim: true)
    }

    options {
        timestamps()
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                cleanWs()
                echo "Checkout in workspace: ${WORKSPACE}"
                git branch: "${TAF_BRANCH}", url: ''
            }
        }

        stage('Run Maven Tests') {
            steps {
                withCredentials([
                        string(credentialsId: 'api.token', variable: 'API_TOKEN')]) {
                    sh "mvn clean test -Dapi.token=${API_TOKEN} -Dselenide.headless=true"
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }
        }
    }
}
