# Verifier
## Description
The Verifier contains the protocol used in the [Cryptodid Android app](https://github.com/Grass15/cryptodid_android_app.git) (in the access-control branch) to execute the [stadium use case](https://github.com/JoshuaAziake/stadium_app.git). 
 
 
 ## Prerequistes

  - [Java 11](https://www.oracle.com/ca-en/java/technologies/javase/jdk11-archive-downloads.html)
    ```terminal 
    set the JAVA_HOME environment variable pointing to java 11 JDK installation or have the java executable on the PATH environment variable
    ```
  - [Maven](https://maven.apache.org/download.cgi?.)
    ```terminal 
    1. Download the latest binary archive version of maven
    2. Extract the distribution archive and rename the folder Maven
      unzip apache-maven-3.9.1-bin.zip
      OR
      tar xzvf apache-maven-3.9.1-bin.tar.gz
    3. Add the bin folder to the PATH environment variable
    4. Confirm with the command mvn -v in a new shell
    ```
 - [Git](https://git-scm.com/download)
   ``` terminal
   Download the latest version
   ```
    
## Setup

1. Open a new shell 
2. Create a copy of the remote repository in your machine
   ```terminal
   git clone https://github.com/Grass15/cryptodid_verifier.git
   git checkout access-control
   ``` 
3. Run the project
   ``` terminal
   cd cryptodid_verifier
   mvn package
   target\bin\webapp (Windows) or sh target/bin/webapp
   ```
   <br>
   <br>
  Good to know:
    
   There are two versions of the Verifier. Heroku doesn't support pure TCP/IP so for the deployed version (on branch Main), 
   the verifier uses websocket to communicate with the android app.
      
  
