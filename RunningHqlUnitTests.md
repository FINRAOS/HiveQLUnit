# Running HqlUnit Tests #

This user guide explains how to run unit test suites constructed with HqlUnit. The environment does matter; different steps are needed based on operating system for example.

## Needed JVM Arguments ##

Several JVM arguments are needed to make HqlUnit test suites correctly work. This section lists the needed arguments, and the below sections 'Running from Command Line' and 'Running in an IDE' will explain how to correctly pass in the needed JVM arguments.

### Windows ###

Running Hive on Windows requires a number of dynamic link libraries for Hadoop. These dlls need to be present for HqlUnit to work properly. A full set of these dlls can be downloaded precompiled from https://codeload.github.com/srccodes/hadoop-common-2.2.0-bin/zip/master. The unziped folder can be placed anywhere, the path to it needed as a JVM argument, 

    -Dhadoop.home.dir="FULL FILE PATH TO HADOOP DLLS FOLDER"
    -Dhadoop.home.dir="C:/Users/YourUserName/hadoop/hadoop-common-2.2.0-bin-master"

Even though the full file path is on a Windows style file system, the backslashes need to be substituted with forward slashes, or Hive will not read the file path correctly.

    C:\Users\YourUserName\hadoop\hadoop-common-2.2.0-bin-master -> C:/Users/k00001/hadoop/hadoop-common-2.2.0-bin-master

### Mac/Linux ###

Running Hive on *nix requires an extant (733 permissions) folder to use as the Hive metastore.

    -Dhive.metastore.warehouse.dir=/tmp/foo

### PermGen Space ###

Regardless of operating system, unit tests run with HqlUnit need at least 128 MB of PermGen space,

    -XX:MaxPermSize=128m

## Running from Command Line ##

Test suites built with HqlUnit can be run from the command line with

	mvn clean test

To run from the command line (or as part of a Maven build) JVM command line arguments need to be specified in the pom.xml file to the surefire plugin:

    <build>
        <plugins>
            
            ...

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
                <configuration>
                    <argLine>ARGUMENTS GO HERE</argLine>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <argLine>-XX:MaxPermSize=128m -Dhive.metastore.warehouse.dir=/tmp/foo</argLine>

## Running in an IDE ##

When unit tests are run from inside an IDE, the IDE will ignore any JVM arguments specified in the pom.xml file to the surefire plugin. The JVM arguments need to be configured into the IDE itself.

### Intellij IDEA ###

1. Go to 'Run' in the top bar menu
2. Under 'Run' select 'Edit Configurations...' - a menu will pop up
3. Select 'Junit' under the Defaults heading in the left side bar of the pop up menu
4. Add the needed JVM arguments to the 'VM Options' text bar
5. Click the 'Apply' button
6. Delete any configurations for individual unit tests, forcing them to all use the defaults
