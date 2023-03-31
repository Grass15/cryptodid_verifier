# Verifier
  
  Guide to setup on your local machine
  
  Prerequistes: 

  Java 11
  =========
    set the JAVA_HOME environment variable pointing to java 11 JDK installation or have the java executable on the PATH environment variable

  Maven
  =============
    Download the latest binary archive version of maven: https://maven.apache.org/download.cgi?.
    Extract the distribution archive and rename the folder Maven
      - unzip apache-maven-3.9.1-bin.zip
        Or
      - tar xzvf apache-maven-3.9.1-bin.tar.gz
    Add the bin folder to the PATH environment variable
    Confirm with the command mvn -v in a new shell
    
Setup:
=============
  
  In a new shell do the following commands
  ### ` git clone https://github.com/Grass15/cryptodid_verifier.git`
  ### ` cd cryptodid_verifier`
  ### ` mvn package`
  ### ` target\bin\webapp (Windows) or sh target/bin/webapp`

  Good to know:
    
    There are two versions of the Verifier. Heroku doesn't support pure TCP/IP. So for the deployed version (On branch Main ), 
    the verifier use websocket to communicate with the android app
      
  
