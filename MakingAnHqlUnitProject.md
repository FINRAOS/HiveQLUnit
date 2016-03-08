# Making an HqlUnit Project #

This user guide covers the steps required to start a new test suite with HqlUnit.

## Required Tools ##

HqlUnit is developed with git as source control and is intended for use as a maven dependency in a Java maven project. Users who do not have these tools or are not familiar with them will have a harder time using HqlUnit in their projects. Before proceeding further though the user guides, a user should make sure they have these tools and are comfortable with them,

1. Java, at least version 7
2. Git
3. Maven

## Installing HqlUnit ##

Releases of HqlUnit can be found in Maven Central, and Maven should automatically handle aquiring HqlUnit releases to use as a dependency.

## Compiling locally ##

However, it can happent that Maven is unable to find the HqlUnit jar to use as a dependency. The user can work around this by installing HqlUnit directly into their local maven dependency repository. HqlUnit uses git for source control, and the official source repository is located at stash.finra.org. The code base can be seen in a web browser at

    https://github.com/FINRAOS/HiveQLUnit

If a user has git installed, a copy of the repository can be cloned to the user's machine with

    git clone https://github.com/FINRAOS/HiveQLUnit

This will make a folder named 'hqlunit-core' with the repository copy in the present working directory. The user should then switch the repository copy branch to the desired release version branch. If the Feb2016 release is desired,

    git checkout Feb2016

Once the source code has been downloaded and the correct release chosen, HqlUnit can be installed with Maven using the command

    mvn clean intsall

This will install the DEV-SNAPSHOT version of HqlUnit in the local Maven dependency repository. HqlUnit can now be used in projects.

## Structuring an HqlUnit Project ##

HqlUnit is intended for use as a Maven dependency in a Java maven project. A test suite built with HqlUnit needs to be structured like any other Maven project. The test suite needs a 'java' and a 'resources' folder,

    ./src/test/java
    ./src/test/resources

Classes with testing code go in src/test/java, and resources (data, scripts, ect) needed during testing go in src/test/resources.

Configuration settings go in a pom.xml file in the root folder of the project.

    ./pom.xml

## Configuring the Pom File ##

The pom.xml file of the new test suite must be configured to work with HqlUnit. HqlUnit must be added as a dependency. The dependency info for the latest December2015 release is

    <dependencies>

        ...

        <dependency>
            <groupId>org.finra.qc</groupId>
            <artifactId>hqlUnit</artifactId>
            <version>December2015</version>
        </dependency>
    </dependencies>

Unit tests run with HqlUnit need at least 128 MB of PermGen space. When running the test suite from the command line (say with mvn clean test), the following pom.xml configuration will configure the surefire plugin to use the correct PermGen space size for unit tests

    <build>
        <plugins>
            
            ...

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.18.1</version>
                <configuration>
                    <argLine>-XX:MaxPermSize=128m</argLine>
                </configuration>
            </plugin>
        </plugins>
    </build>
