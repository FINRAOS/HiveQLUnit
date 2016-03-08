# Making an HiveQLUnit Project #

This user guide covers the steps required to start a new test suite with HiveQLUnit.

## Required Tools ##

HiveQLUnit is developed with git as source control and is intended for use as a maven dependency in a Java maven project. Users who do not have these tools or are not familiar with them will have a harder time using HiveQLUnit in their projects. Before proceeding further though the user guides, a user should make sure they have these tools and are comfortable with them,

1. Java, at least version 7
2. Git
3. Maven

## Installing HiveQLUnit ##

Releases of HiveQLUnit can be found in Maven Central, and Maven should automatically handle acquiring HiveQLUnit releases to use as a dependency.

## Structuring an HiveQLUnit Project ##

HiveQLUnit is intended for use as a Maven dependency in a Java maven project. A test suite built with HiveQLUnit needs to be structured like any other Maven project. The test suite needs a 'java' and a 'resources' folder,

    ./src/test/java
    ./src/test/resources

Classes with testing code go in src/test/java, and resources (data, scripts, ect) needed during testing go in src/test/resources.

Configuration settings go in a pom.xml file in the root folder of the project.

    ./pom.xml

## Configuring the Pom File ##

The pom.xml file of the new test suite must be configured to work with HiveQLUnit. HiveQLUnit must be added as a dependency. The dependency info for the latest 1.0 release is

    <dependencies>

        ...

        <dependency>
            <groupId>org.finra.hiveqlunit</groupId>
            <artifactId>hiveQLUnit</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>

Unit tests run with HiveQLUnit need at least 128 MB of PermGen space. When running the test suite from the command line (say with mvn clean test), the following pom.xml configuration will configure the surefire plugin to use the correct PermGen space size for unit tests

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