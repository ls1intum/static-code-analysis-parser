# static-code-analysis-parser
Common parser functionality for static code analysis tools on continuous integration servers to read reports and send the data to Artemis

## Deployment

1.  Make sure you have a Sonatype JIRA account. Store the credentials in the settings.xml in the m2 home folder
2.  Make sure you have a valid GPG certificate (follow the instructions on https://central.sonatype.org/publish/requirements/gpg)
3.  Store the used key in your Maven settings.xml file, usually placed in ~/.m2: https://central.sonatype.org/publish/publish-maven
4.  Run `export GPG_TTY=$(tty)`
5.  Run `mvn clean deploy -P release`  
    This should create the jar file, sources, javadoc and the pom file. All files should also have an additional signed *.asc file. You might find the files in your m2 home folder. 
6.  Verify the artifact and release it to Maven Central. Open https://oss.sonatype.org/#stagingRepositories, close the repository to start the validation and as soon as the validation has finished successfully, release the artifact.
