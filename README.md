# static-code-analysis-parser
Common parser functionality for static code analysis tools on continuous integration servers to read reports and send the data to Artemis

# Deployment

1. Make sure you have a Sonatype JIRA account. Store the credentials in the settings.xml in the m2 home folder
2. Make sure you have a valid GPG certificate (follow the instructions on https://central.sonatype.org/publish/requirements/gpg)
3. Run `mvn clean install -P release`
   This should create the jar file, sources, javadoc and the pom file. All files should also have an additional signed *.asc file. You might find the files in your m2 home folder. 
4. Upload the artifact to Maven Central
    a. Do it automatically using `mvn deploy` (does not work at the moment)
    b. Upload all files to the staging area on https://oss.sonatype.org/#stagingRepositories and release them manually
