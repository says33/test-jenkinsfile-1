def call(Map config = [:]){
    
    node{

        String repoUrl = config.get('repoUrl')
        String branch = config.get('branch', 'main')
        String credentialsId = config.get('credentialsId', 'GIT_SSH_CREDENTIALS')
        String mavenComm = config.get('mavenComm', 'clean verify')
        boolean executeSonar = config.get('execSonar', true)
        String sonarUrl = config.get('sonarHost')
        String sonarOpt = config.get('sonarOpt', '')

        stage('checkout') {
            checkoutRepo(repoUrl:repoUrl, branch: branch, credentialsId: credentialsId)
        }

        stage('Build & test') {
            sh "mvn -B ${mavenComm}"
        }

        if (executeSonar) {
            stage("SonarQuebe scan") {
                withCredentials([string(credentialsId: "SONAR_TOKEN", variable: "SONAR_TOKEN")]) {
                    def sonarProps = "-Dsonar.login=${env.SONAR_TOKEN}"
                    
                    if (sonarUrl) {
                        sonarProps += "-Dsonar.host.url=${sonarUrl}"
                    }
                    if (sonarOpt) {
                        sonarProps += "${sonarOpt}"
                    }

                    sh "mvn -B sonar:sonar ${sonarProps}"

                }
            }
        }

        stage('Archive') {
            archiveArtifact artifacts: 'target/*.jar', allowEmptyArchive: true
            junit '**/target/surefire-reports/*.xml'
        }

    }

}