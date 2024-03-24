def call() {
    pipeline {

        agent {
            node {
                label 'workstation'
            }
        }

        options {
            ansiColor('xterm')
        }

        parameters {
            choice(name: 'env', choices: ['dev', 'prod'], description: 'pick environment')
            choice(name: 'action', choices: ['apply', 'destroy'], description: 'pick Action')
        }

        stages {
            stage('Terraform INIT') {
                steps {
                    sh 'terraform init -backend-config=env-${env}/state.tfvars'
                }
            }
            stage('Terraform APPLY') {
                steps {
                    sh 'terraform ${action} -auto-approve -var-file=env-${env}/main.tfvars'
                }
            }
//            stage('Terraform DESTROY') {
//                steps {
//                    sh 'terraform destroy -auto-approve -var-file=env-${env}/main.tfvars'
        }

        post {
            always {
                cleanWs()
            }
        }
    }
}