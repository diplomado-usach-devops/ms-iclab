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

					if(params.builTools == 'gradle')
					{
					    def ejecucion = load 'gradle.groovy'
	                                    ejecucion.call()
					}
					else
					{
					    def ejecucion = load 'maven.groovy'
	                                    ejecucion.call()
					}
				}
			}
		}		
		
	}
}

}

return this;