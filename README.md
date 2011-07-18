## History

Author: Zach Bailey, Bradley Wagner

Version: 0.3

Version History:

- 0.1 Initial Version
- 0.2 Updating instructions to use custom property to specify lib locations instead of adding libs manually
- 0.3 Adding Maven

## Prerequisites 

This assumes you have Eclipse 3.6+, m2e Maven plugin, and Ant 1.7+ installed and configured
for your development environment, as well as the included Axis libraries.

- Eclipse can be downloaded at: http://download.eclipse.org
- Ant can be downloaded at: http://ant.apache.org/
- m2e Maven plugin comes with Eclipse 3.7+ and can be installed for older versions by going to Help > Eclipse Marketplace and typing 'm2e'

## Environment 

It is recommended you set up your environment in the following manner:

1.) Create a "java" directory. Unzip Ant, Eclipse into it.
2.) Add the ant executable to your path. You should be able to type "ant" on the command line
	if it's been correctly added to your path:
	$ ant -version
	Apache Ant(TM) version 1.8.2 compiled on December 20 2010
	
3.) Create a "workspace" folder in your java folder. This will be the root of your Eclipse workspace.
	When you start Eclipse for the first time, tell it to use this folder as the workspace folder. If
	you are already using Eclipse and have already designated a workspace folder, you can continue using
	that folder.
	
Next we will bring the project into Eclipse. To do this:

1.) Unzip/move the contents of this zip file to your Eclipse workspace directory.
2.) Import the project into Eclipse by choosing File -> Import -> General -> Existing Projects into Workspace 
	and choosing the "Cascade Webservices" folder (this is the root of the project).

After this you should see the Cascade Webservices project show up in your Eclipse window. Do not be alarmed if 
there are compilation errors, this is because you still need to generate the Java client stubs for interacting 
with the web services layer.

To do this:

1. Open the WSDL from your Cascade Server instance by going to: http://<your-cascade-url>/ws/services/AssetOperationService?wsdl
2. Save this as a file "asset-operation.wsdl".
3. Replace the "asset-operation.wsdl" file in src/java/wsdl inside the eclipse project with your own file.
4. Open a command-line/terminal window to run ant. 
5. Navigate to to the base directory where the project was unzipped to (e.g. java/workspace/Cascade Webservices) and type the command "ant"
You should see a successful ant build similar to:

	$ ant
	Buildfile: /Users/bradley/cascade/Webservices-Java-Sample-Project/build.xml
	
	clean:
	   [delete] Deleting directory /Users/bradley/cascade/Webservices-Java-Sample-Project/src/gen
	
	generate-stubs:
	    [mkdir] Created dir: /Users/bradley/cascade/Webservices-Java-Sample-Project/src/gen
	[wsdl2java] WSDL2Java /Users/bradley/cascade/Webservices-Java-Sample-Project/src/java/wsdl/asset-operation.wsdl
	
	BUILD SUCCESSFUL
	Total time: 5 seconds
6. Refresh the project by right clicking on the project and selecting "Refresh".

At this point Eclipse will re-build the project for you. You should not see any red x's
on any files (which indicate java compilation errors). 

## To run an example
 
1. Open the TestRead class
2. Change the credentials to those of a user that has access to the Base Folder in the Global Area
3. Right click the main() method > Run as... > Java Application