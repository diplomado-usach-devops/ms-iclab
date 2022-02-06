/*
    forma de invocación de método call:
    def ejecucion = load 'script.groovy'
    ejecucion.call()
*/

def call(String pipeliType){
  
    String[] str    
        str = params.stage.split(';')
    
    
    def bandera = true
    for (int i = 0; i < str.size(); i++) {
        switch(str[0]) {
            case "unitTest":
            case "jar":
            case "sonar":           
            case "nexusUpload":
            case "gitCreateRelease":
            case "":
                bandera = true
          
             default:
                bandera = false
                break
        }   
    }
    
    
    figlet params.buildTool
    figlet env.GIT_BRANCH
    //println bandera
    figlet pipeliType
    
    figlet "github"
    stage('Checkout code') {        
            def git = new helpers.Git()
            git.remote
    }
    
    if(pipeliType == 'CD')
    {
        stage('Download Nexus') {
               figlet 'Download Nexus'
                bat "curl -L  -u admin:123456 http://localhost:8081/repository/test-repo/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar --output DevOpsUsach2020-0.0.1.jar" 
         }       

        stage('Test Code') { 
               figlet 'TestBuild'
               bat "gradle Build"
        }       
        
        stage('SonarQube analysis') { 
                    figlet 'SonarQube'
                    def scannerHome = tool 'sonar-scanner';
                    withSonarQubeEnv('sonar-server') { 
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build " 
                    }           
        }
    
    }
    else
    {
    if(bandera){
        if(str.contains('compile') || params.stage.isEmpty() )
        {   
            stage('Compile') {  
                figlet 'Compile'
                        bat "mvn clean compile -e"
                }
        }

        

        if(str.contains('unitTest') || params.stage.isEmpty())
        {
            stage('unitTest') {
                 figlet 'unitTest'
                bat "mvn clean test -e" 
            }
        }
        
        if(str.contains('code') || params.stage.isEmpty())
        {
            stage('Jar') {
                 figlet 'Jar'
                            bat "mvn clean package -e"            
                }
        }
        
        if(env.GIT_BRANCH == 'develop'){
            figlet 'gitCreateRelease'
            bat "git config --global user.email 'srivera.r03@gmail.com'"
            bat "git config --global user.name 'sriverar03'"
            bat "git checkout -b prueba"
            bat "git add ."
            bat "git commit -m 'se agregar nueva rama'"
            bat "git push origin prueba" 
        }
        
        if(str.contains('sonar') || params.stage.isEmpty())
        {
            stage('Sonar') { 
                 figlet 'Sonar'
                    def scannerHome = tool 'sonar-scanner';
                    withSonarQubeEnv('sonar-server') { 
                    bat "${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.sources=src -Dsonar.java.binaries=build " 
                }
           
                }
        }
        
       
        
        /*if(str.contains('run') || params.stage.isEmpty())
        {
             stage('Run Jar') { 
                  figlet 'Run Jar'
                            bat "start /min mvn spring-boot:run &"           
                }
        }*/

        if(str.contains('nexusUpload') || params.stage.isEmpty())
        {
            stage('nexusUpload') {
                 figlet 'Nexus Upload'
                bat "curl -v --user admin:123456 --upload-file C:/Users/nmt02/.jenkins/workspace/pipilene_sonar_feature-sonar/build/DevOpsUsach2020-0.0.1.jar http://7fb6-186-79-184-102.ngrok.io/repository/test-repo/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar "            
            } 
        }
        
         




        /*stage('Test Application') {           
                    bat "curl -X GET http://localhost:8081/rest/mscovid/test?msg=testing"             
        }*/
    }
    else{
        println 'verifique hay stages ingresados que no existen.'
    }
   }
}

return this;
