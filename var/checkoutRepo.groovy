def call(Map config = [:]) {

    String repoUrl = config.get('repoUrl')
    String branch = config.get('branch', 'main')
    String credentialsId = config.get('credentialsId', 'GIT_SSH_CREDENTIALS')

    if (!repoUrl) error "Checkout Repo Step Fail(repoUrl Required)"

    sshagent([credentialsId]) {
        checkout([$class: 'GitSCM']){
            branches: [[name: "*/${branches}"]],
            userRemoteConfig: [[url: repoUrl, credentialsId: credentialsId]]
        }
    }

}