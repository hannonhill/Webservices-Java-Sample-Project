## History

Author: Zach Bailey, Bradley Wagner

Version History:

- 0.1 Initial Version
- 0.2 Updating instructions to use custom property to specify lib locations instead of adding libs manually
- 0.3 Adding Maven

## Installation

### In Eclipse

This assumes you have Eclipse 3.6+, m2e Maven plugin, Maven, and Git installed.

- Eclipse can be downloaded at: http://download.eclipse.org
- Maven can be downloaded at: http://maven.apache.org/download.html
- m2e Maven plugin comes with Eclipse 3.7+ and can be installed for older versions by going to Help > Eclipse Marketplace and typing 'm2e'
- Git can be downloaded at: http://git-scm.com/download

It is recommended you set up your environment in the following manner:

1. Create a "java" directory. Unzip Eclipse into it.
2. Ensue the "maven" executable is in your path. Type "mvn" on the command line if it's been correctly added to your path:

        $ mvn -v
        Apache Maven 3.0.2 (r1056850; 2016-01-08 19:58:10-0500)
        Java version: 1.6.0_24, vendor: Apple Inc.
        Java home: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
        Default locale: en_US, platform encoding: MacRoman
        OS name: "mac os x", version: "10.6.8", arch: "x86_64", family: "mac"
	
3. Create a "workspace" folder in your java folder. This will be the root of your Eclipse workspace. 
   When you start Eclipse for the first time, tell it to use this folder as the workspace folder. If
   you are already using Eclipse and have already designated a workspace folder, you can continue using that folder.

Then you need to install 2 additional connectors from the m2e Marketplace in Eclipse:

1. File > Import > Check out Maven Projects from SCM > Next
2. Click "m2e Marketplace" link
3. Select the "Axis Tools m2e" and "m2e-egit" connectors from the m2e Marketplace
4. Click Finish
5. Follow the prompts to install these connectors
6. Restart Eclipse once these are done installing

Once Eclipse restarts:

1. File > Import > Check out Maven Projects from SCM > Next
2. Select "git" from the dropdown
3. Enter git://github.com/hannonhill/Webservices-Java-Sample-Project.git into the Git URL field
4. Click Next
5. Click Finish

This will import and build the project in Eclipse.

Once built, use Git to checkout the appropriate branch of this project for your Cascade installation. For example, if you're running Cascade version 8.0.1, checkout 8.0.x: `git checkout 8.0.x`

To update the generated Web Services stubs to correspond to your version of Cascade:

1. Open the WSDL from your Cascade Server instance by going to: http://your-cascade-url/ws/services/AssetOperationService?wsdl
2. Save this as a file "asset-operation.wsdl".
3. Replace the "asset-operation.wsdl" file in src/java/wsdl inside the eclipse project with your own file.
4. In Eclipse, right-click the project and click Refresh. Maven should run and regenerate your stubs based on this updated WSDL file.
5. If for some reason it doesn't, open a command-line/terminal window to run maven.
6. Navigate to to the base directory where the project was created to (e.g. java/workspace/Cascade Webservices) and type the command "mvn generate-sources"
You should see a successful ant build similar to:

		$ mvn generate-sources
        [INFO] Scanning for projects...
        [INFO]                                                                         
        [INFO] ------------------------------------------------------------------------
        [INFO] Building Cascade-Java-Web-Services-Example-Project 8.0.1
        [INFO] ------------------------------------------------------------------------
        [INFO] 
        [INFO] --- axistools-maven-plugin:1.4:wsdl2java (default) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] about to add compile source root
        [INFO] Processing wsdl: /Users/bradley/cascade/Webservices-Java-Sample-Project/src/java/wsdl/asset-operation.wsdl
        Jul 18, 2016 3:33:52 PM org.apache.axis.utils.JavaUtils isAttachmentSupported
        WARNING: Unable to find required classes (javax.activation.DataHandler and javax.mail.internet.MimeMultipart). Attachment support is disabled.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 3.940s
        [INFO] Finished at: Mon Jul 18 15:33:55 EDT 2016
        [INFO] Final Memory: 3M/81M
        [INFO] ------------------------------------------------------------------------
        
7. Then, refresh Eclipse and your project should be built.

### With Git and Maven only

This assumes you have Maven 3+ and Git installed. 

- Maven can be downloaded at: http://maven.apache.org/download.html
- Git can be downloaded at: http://git-scm.com/download

Clone the project:

1. Clone this repository: git clone git://github.com/hannonhill/Webservices-Java-Sample-Project.git
2. Change into the directory for the newly created project
3. Use Git to checkout the appropriate branch of this project for your Cascade installation. For example, if you're running Cascade version 8.0.1, checkout 8.0.x: `git checkout 8.0.x`

Update the Web Services stubs:

1. Open the WSDL from your Cascade Server instance by going to: http://<your-cascade-url>/ws/services/AssetOperationService?wsdl
2. Save this as a file "asset-operation.wsdl".
3. Replace the "asset-operation.wsdl" file in src/java/wsdl inside the eclipse project with your own file.
4. Open a command-line/terminal window to run maven. 
5. Navigate to to the base directory where the project was unzipped to (e.g. java/workspace/Cascade Webservices) and type the command "mvn generate-sources"
You should see a successful ant build similar to:

		$ mvn generate-sources
        [INFO] Scanning for projects...
        [INFO]                                                                         
        [INFO] ------------------------------------------------------------------------
        [INFO] Building Cascade-Java-Web-Services-Example-Project 8.0.1
        [INFO] ------------------------------------------------------------------------
        [INFO] 
        [INFO] --- axistools-maven-plugin:1.4:wsdl2java (default) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] about to add compile source root
        [INFO] Processing wsdl: /Users/bradley/cascade/Webservices-Java-Sample-Project/src/java/wsdl/asset-operation.wsdl
        Jul 18, 2016 3:33:52 PM org.apache.axis.utils.JavaUtils isAttachmentSupported
        WARNING: Unable to find required classes (javax.activation.DataHandler and javax.mail.internet.MimeMultipart). Attachment support is disabled.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 3.940s
        [INFO] Finished at: Mon Jul 18 15:33:55 EDT 2016
        [INFO] Final Memory: 3M/81M
        [INFO] ------------------------------------------------------------------------

## To run an example in Eclipse
 
1. Open the TestRead class
2. Change the credentials to those of a user that has access to the Base Folder in a Site
3. Enter the name of the site where it says "\<SITE-NAME\>"
4. Right click the testBaseFolderRead() method > Run as... > JUnit Test

## To run a test with Maven

1. Open the TestRead class
2. Change the credentials to those of a user that has access to the Base Folder in a Site
3. Enter the name of the site where it says "\<SITE-NAME\>"
4. Open a terminal
5. Navigate to the project folder
6. Type: `mvn -Dtest=TestRead test`
You should see something like:

        $ mvn -Dtest=TestRead test
        [INFO] Scanning for projects...
        [INFO]                                                                         
        [INFO] ------------------------------------------------------------------------
        [INFO] Building Cascade-Java-Web-Services-Example-Project 8.0.1
        [INFO] ------------------------------------------------------------------------
        [WARNING] The artifact axis:axis-jaxrpc:jar:1.4 has been relocated to org.apache.axis:axis-jaxrpc:jar:1.4
        [INFO] 
        [INFO] --- axistools-maven-plugin:1.4:wsdl2java (default) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] about to add compile source root
        [INFO] Nothing to generate. All WSDL files are up to date.
        [INFO] 
        [INFO] --- maven-resources-plugin:2.4.3:resources (default-resources) @ Cascade-Java-Web-Services-Example-Project ---
        [WARNING] Using platform encoding (MacRoman actually) to copy filtered resources, i.e. build is platform dependent!
        [INFO] skip non existing resourceDirectory /Users/bradley/cascade/Cascade-Java-Web-Services-Example-Project/src/main/resources
        [INFO] 
        [INFO] --- maven-compiler-plugin:2.3.2:compile (default-compile) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] Nothing to compile - all classes are up to date
        [INFO] 
        [INFO] --- maven-resources-plugin:2.4.3:testResources (default-testResources) @ Cascade-Java-Web-Services-Example-Project ---
        [WARNING] Using platform encoding (MacRoman actually) to copy filtered resources, i.e. build is platform dependent!
        [INFO] skip non existing resourceDirectory /Users/bradley/cascade/Cascade-Java-Web-Services-Example-Project/src/test/resources
        [INFO] 
        [INFO] --- maven-compiler-plugin:2.3.2:testCompile (default-testCompile) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] Nothing to compile - all classes are up to date
        [INFO] 
        [INFO] --- maven-surefire-plugin:2.7.1:test (default-test) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] Surefire report directory: /Users/bradley/cascade/Cascade-Java-Web-Services-Example-Project/target/surefire-reports

        -------------------------------------------------------
         T E S T S
        -------------------------------------------------------
        Running com.cms.webservices.TestRead
        Jul 18, 2016 5:37:31 PM org.apache.axis.utils.JavaUtils isAttachmentSupported
        WARNING: Unable to find required classes (javax.activation.DataHandler and javax.mail.internet.MimeMultipart). Attachment support is disabled.
        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.316 sec

        Results :

        Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 4.752s
        [INFO] Finished at: Mon Jul 18 17:37:32 EDT 2016
        [INFO] Final Memory: 4M/81M
        [INFO] ------------------------------------------------------------------------
