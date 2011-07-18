## History

Author: Zach Bailey, Bradley Wagner

Version: 0.3

Version History:

- 0.1 Initial Version
- 0.2 Updating instructions to use custom property to specify lib locations instead of adding libs manually
- 0.3 Adding Maven

## Prerequisites 

This assumes you have Eclipse 3.6+, m2e Maven plugin, and Maven installed and configured
for your development environment

- Eclipse can be downloaded at: http://download.eclipse.org
- Maven can be downloaded at: http://maven.apache.org/download.html
- m2e Maven plugin comes with Eclipse 3.7+ and can be installed for older versions by going to Help > Eclipse Marketplace and typing 'm2e'

## Environment 

It is recommended you set up your environment in the following manner:

1. Create a "java" directory. Unzip Eclipse into it.
2. Ensue the "maven" executable is in your path. Type "mvn" on the command line if it's been correctly added to your path:

	$ mvn -v
    Apache Maven 3.0.2 (r1056850; 2011-01-08 19:58:10-0500)
    Java version: 1.6.0_24, vendor: Apple Inc.
    Java home: /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home
    Default locale: en_US, platform encoding: MacRoman
    OS name: "mac os x", version: "10.6.8", arch: "x86_64", family: "mac"
	
3. Create a "workspace" folder in your java folder. This will be the root of your Eclipse workspace. 
   When you start Eclipse for the first time, tell it to use this folder as the workspace folder. If
   you are already using Eclipse and have already designated a workspace folder, you can continue using
   that folder.
	
Next we will bring the project into Eclipse. To do this:

1. Unzip/move the contents of this zip file to your Eclipse workspace directory.
2. Import the project into Eclipse by choosing File -> Import -> General -> Existing Projects into Workspace 
   and choosing the "Cascade Webservices" folder (this is the root of the project).

After this you should see the Cascade Webservices project show up in your Eclipse window. Do not be alarmed if 
there are compilation errors, this is because you still need to generate the Java client stubs for interacting 
with the web services layer.

To do this:

1. Open the WSDL from your Cascade Server instance by going to: http://<your-cascade-url>/ws/services/AssetOperationService?wsdl
2. Save this as a file "asset-operation.wsdl".
3. Replace the "asset-operation.wsdl" file in src/java/wsdl inside the eclipse project with your own file.
4. Open a command-line/terminal window to run maven. 
5. Navigate to to the base directory where the project was unzipped to (e.g. java/workspace/Cascade Webservices) and type the command "maven generate-sources"
You should see a successful ant build similar to:

		$ maven generate-sources
        [INFO] Scanning for projects...
        [INFO]                                                                         
        [INFO] ------------------------------------------------------------------------
        [INFO] Building Cascade-Java-Web-Services-Example-Project 6.8.3
        [INFO] ------------------------------------------------------------------------
        [INFO] 
        [INFO] --- axistools-maven-plugin:1.4:wsdl2java (default) @ Cascade-Java-Web-Services-Example-Project ---
        [INFO] about to add compile source root
        [INFO] Processing wsdl: /Users/bradley/cascade/Webservices-Java-Sample-Project/src/java/wsdl/asset-operation.wsdl
        Jul 18, 2011 3:33:52 PM org.apache.axis.utils.JavaUtils isAttachmentSupported
        WARNING: Unable to find required classes (javax.activation.DataHandler and javax.mail.internet.MimeMultipart). Attachment support is disabled.
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD SUCCESS
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time: 3.940s
        [INFO] Finished at: Mon Jul 18 15:33:55 EDT 2011
        [INFO] Final Memory: 3M/81M
        [INFO] ------------------------------------------------------------------------
	
6. Refresh the project by right clicking on the project and selecting "Refresh".

At this point Eclipse will re-build the project for you. You should not see any red x's
on any files (which indicate java compilation errors). 

## To run an example
 
1. Open the TestRead class
2. Change the credentials to those of a user that has access to the Base Folder in the Global Area
3. Right click the main() method > Run as... > Java Application