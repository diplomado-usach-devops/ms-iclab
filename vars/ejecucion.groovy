/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()
*/

def call(){
  
  pipeline {

	agent any

	parameters {
  		choice choices: ['gradle', 'maven'], description: 'indicar herramienta de construccion', name: 'buildTool'
  		string(defaultValue: '', name: 'stage', trim: true)
	}

	stages{
		stage('Pipeline'){
			steps{
				script{				    
					println "Pipeline"

					if(params.buildTool == 'gradle')
					{
					   gradle(BranchName())
					}
					else
					{
					    maven(BranchName())
					}
				}
			}
		}		
		
	}
}

}

post {
	
	
			success {
				slackSend color: 'good', message: "[Grupo1][Pipeline prueba1][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado OK]"
			}
			failure {
				slackSend color: 'danger', message: "[Grupo1][Pipeline prueba2][Rama: ${env.GIT_BRANCH}][Stage: ${STAGE}][Resultado No OK]"
				
			}
		}
} 


def BranchName(){
	if(env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop'))
	{ return 'CI' }
	else
	{ return 'Release' }
	
}

return this;
