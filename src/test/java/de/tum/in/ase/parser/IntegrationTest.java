package de.tum.in.ase.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import de.tum.in.ase.parser.exception.ParserException;

/**
 * Tests each parser with an example file
 */
public class IntegrationTest {

    private static final String EXPECTED_CHECKSTYLE = "{\"tool\":\"CHECKSTYLE\",\"issues\":[{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":6,\"endLine\":6,\"startColumn\":8,\"endColumn\":8,\"rule\":\"UnusedImportsCheck\",\"category\":\"imports\",\"message\":\"Unused import - javax.swing.JFrame.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":7,\"endLine\":7,\"startColumn\":1,\"endColumn\":1,\"rule\":\"RedundantImportCheck\",\"category\":\"imports\",\"message\":\"Redundant import from the java.lang package - java.lang.*.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":65,\"endLine\":65,\"startColumn\":0,\"endColumn\":0,\"rule\":\"RegexpSinglelineCheck\",\"category\":\"regexp\",\"message\":\"Line has trailing spaces.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":68,\"endLine\":68,\"startColumn\":0,\"endColumn\":0,\"rule\":\"RegexpSinglelineCheck\",\"category\":\"regexp\",\"message\":\"Line has trailing spaces.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":70,\"endLine\":70,\"startColumn\":0,\"endColumn\":0,\"rule\":\"RegexpSinglelineCheck\",\"category\":\"regexp\",\"message\":\"Line has trailing spaces.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":70,\"endLine\":70,\"startColumn\":9,\"endColumn\":9,\"rule\":\"NeedBracesCheck\",\"category\":\"blocks\",\"message\":\"'for' construct must use '{}'s.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":73,\"endLine\":73,\"startColumn\":0,\"endColumn\":0,\"rule\":\"RegexpSinglelineCheck\",\"category\":\"regexp\",\"message\":\"Line has trailing spaces.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":76,\"endLine\":76,\"startColumn\":0,\"endColumn\":0,\"rule\":\"RegexpSinglelineCheck\",\"category\":\"regexp\",\"message\":\"Line has trailing spaces.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":78,\"endLine\":78,\"startColumn\":8,\"endColumn\":8,\"rule\":\"JavadocMethodCheck\",\"category\":\"javadoc\",\"message\":\"Unused @param tag for 'x1'.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":80,\"endLine\":80,\"startColumn\":0,\"endColumn\":0,\"rule\":\"JavadocMethodCheck\",\"category\":\"javadoc\",\"message\":\"@return tag should be present and have description.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":80,\"endLine\":80,\"startColumn\":31,\"endColumn\":31,\"rule\":\"MethodNameCheck\",\"category\":\"naming\",\"message\":\"Name 'CreateRandomDatesList' must match pattern '^[a-z][a-zA-Z0-9]*$'.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":93,\"endLine\":93,\"startColumn\":9,\"endColumn\":9,\"rule\":\"EmptyStatementCheck\",\"category\":\"coding\",\"message\":\"Empty statement.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":93,\"endLine\":93,\"startColumn\":10,\"endColumn\":10,\"rule\":\"EmptyStatementCheck\",\"category\":\"coding\",\"message\":\"Empty statement.\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":93,\"endLine\":93,\"startColumn\":11,\"endColumn\":11,\"rule\":\"EmptyStatementCheck\",\"category\":\"coding\",\"message\":\"Empty statement.\",\"priority\":\"error\"}]}";

    private static final String EXPECTED_PMD_CPD = "{\"tool\":\"PMD_CPD\",\"issues\":[{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":60,\"endLine\":75,\"startColumn\":52,\"endColumn\":18,\"rule\":\"Copy/Paste Detection\",\"category\":\"Copy/Paste Detection\",\"message\":\"Code duplication of 16 lines in the following files:\\n1. Client.java:60-75\\n2. Client.java:75-97\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":75,\"endLine\":97,\"startColumn\":53,\"endColumn\":18,\"rule\":\"Copy/Paste Detection\",\"category\":\"Copy/Paste Detection\",\"message\":\"Code duplication of 16 lines in the following files:\\n1. Client.java:60-75\\n2. Client.java:75-97\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/BubbleSort.java\",\"startLine\":13,\"endLine\":24,\"startColumn\":9,\"endColumn\":9,\"rule\":\"Copy/Paste Detection\",\"category\":\"Copy/Paste Detection\",\"message\":\"Code duplication of 12 lines in the following files:\\n1. BubbleSort.java:13-24\\n2. BubbleSort.java:25-36\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/BubbleSort.java\",\"startLine\":25,\"endLine\":36,\"startColumn\":9,\"endColumn\":9,\"rule\":\"Copy/Paste Detection\",\"category\":\"Copy/Paste Detection\",\"message\":\"Code duplication of 12 lines in the following files:\\n1. BubbleSort.java:13-24\\n2. BubbleSort.java:25-36\"}]}";

    private static final String EXPECTED_PMD = "{\"tool\":\"PMD\",\"issues\":[{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":6,\"endLine\":6,\"startColumn\":1,\"endColumn\":26,\"rule\":\"UnusedImports\",\"category\":\"Best Practices\",\"message\":\"Avoid unused imports such as 'javax.swing.JFrame'\",\"priority\":\"4\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":7,\"endLine\":7,\"startColumn\":1,\"endColumn\":19,\"rule\":\"DontImportJavaLang\",\"category\":\"Code Style\",\"message\":\"Avoid importing anything from the package java.lang\",\"priority\":\"4\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":7,\"endLine\":7,\"startColumn\":1,\"endColumn\":19,\"rule\":\"UnusedImports\",\"category\":\"Best Practices\",\"message\":\"Avoid unused imports such as 'java.lang'\",\"priority\":\"4\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":73,\"endLine\":73,\"startColumn\":18,\"endColumn\":27,\"rule\":\"UnusedLocalVariable\",\"category\":\"Best Practices\",\"message\":\"Avoid unused local variables such as 'randomDate'.\",\"priority\":\"3\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":81,\"endLine\":81,\"startColumn\":31,\"endColumn\":53,\"rule\":\"UnusedPrivateMethod\",\"category\":\"Best Practices\",\"message\":\"Avoid unused private methods such as 'CreateRandomDatesList()'.\",\"priority\":\"3\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":94,\"endLine\":94,\"startColumn\":9,\"endColumn\":9,\"rule\":\"EmptyStatementNotInLoop\",\"category\":\"Error Prone\",\"message\":\"An empty statement (semicolon) not part of a loop\",\"priority\":\"3\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":94,\"endLine\":94,\"startColumn\":10,\"endColumn\":10,\"rule\":\"EmptyStatementNotInLoop\",\"category\":\"Error Prone\",\"message\":\"An empty statement (semicolon) not part of a loop\",\"priority\":\"3\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":94,\"endLine\":94,\"startColumn\":11,\"endColumn\":11,\"rule\":\"EmptyStatementNotInLoop\",\"category\":\"Error Prone\",\"message\":\"An empty statement (semicolon) not part of a loop\",\"priority\":\"3\"}]}";

    private static final String EXPECTED_SPOTBUGS = "{\"tool\":\"SPOTBUGS\",\"issues\":[{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":73,\"endLine\":73,\"rule\":\"DLS_DEAD_LOCAL_STORE\",\"category\":\"STYLE\",\"message\":\"Dead store to randomDate in code.Client.createRandomDatesList()\",\"priority\":\"2\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":82,\"endLine\":95,\"rule\":\"NM_METHOD_NAMING_CONVENTION\",\"category\":\"BAD_PRACTICE\",\"message\":\"The method name code.Client.CreateRandomDatesList() doesn't start with a lower case letter\",\"priority\":\"3\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTCODE-ARTEMISTESTUSER1-JOB1/assignment/src/code/Client.java\",\"startLine\":82,\"endLine\":95,\"rule\":\"UPM_UNCALLED_PRIVATE_METHOD\",\"category\":\"PERFORMANCE\",\"message\":\"Private method code.Client.CreateRandomDatesList() is never called\",\"priority\":\"2\"}]}";

    private static final String EXPECTED_SWIFTLINT = "{\"tool\":\"SWIFTLINT\",\"issues\":[{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTSWIFT-ARTEMISTESTUSER1-JOB1/assignment/Sources/swiftLib/Client.swift\",\"startLine\":59,\"endLine\":59,\"startColumn\":0,\"endColumn\":0,\"rule\":\"line_length\",\"category\":\"swiftLint\",\"message\":\"Line should be 120 characters or less: currently 184 characters\",\"priority\":\"error\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTSWIFT-ARTEMISTESTUSER1-JOB1/assignment/Sources/swiftLib/Client.swift\",\"startLine\":58,\"endLine\":58,\"startColumn\":22,\"endColumn\":22,\"rule\":\"trailing_semicolon\",\"category\":\"swiftLint\",\"message\":\"Lines should not have trailing semicolons.\",\"priority\":\"warning\"},{\"filePath\":\"/opt/bambooagent/bamboo-agent-home/xml-data/build-dir/MDTESTSWIFT-ARTEMISTESTUSER1-JOB1/assignment/Sources/swiftLib/Client.swift\",\"startLine\":60,\"endLine\":60,\"startColumn\":35,\"endColumn\":35,\"rule\":\"trailing_semicolon\",\"category\":\"swiftLint\",\"message\":\"Lines should not have trailing semicolons.\",\"priority\":\"warning\"}]}";

    private static final String EXPECTED_INVALID_FILENAME = "{\"tool\":null,\"issues\":[{\"filePath\":\"cpd_invalid.txt\",\"startLine\":1,\"rule\":\"ExceptionDuringParsing\",\"category\":\"miscellaneous\",\"message\":\"An exception occurred during parsing the report for file cpd_invalid.txt. Exception: java.lang.IllegalArgumentException: File must be xml format\"}]}";

    private static final String EXPECTED_INVALID_XML_GER = "{\"tool\":null,\"issues\":[{\"filePath\":\"invalid_xml.xml\",\"startLine\":1,\"rule\":\"ExceptionDuringParsing\",\"category\":\"miscellaneous\",\"message\":\"An exception occurred during parsing the report for file invalid_xml.xml. Exception: org.xml.sax.SAXParseException; systemId: file:/D:/static-code-analysis-parser/src/test/java/invalid_xml.xml; lineNumber: 4; columnNumber: 3; Elementtyp \\\"file\\\" muss mit dem entsprechenden Endtag \\\"</file>\\\" beendet werden.\"}]}";

    private static final String EXPECTED_INVALID_XML_ENG = "{\"tool\":null,\"issues\":[{\"filePath\":\"invalid_xml.xml\",\"startLine\":1,\"rule\":\"ExceptionDuringParsing\",\"category\":\"miscellaneous\",\"message\":\"An exception occurred during parsing the report for file invalid_xml.xml. Exception: org.xml.sax.SAXParseException; systemId: file:/home/runner/work/static-code-analysis-parser/static-code-analysis-parser/src/test/java/invalid_xml.xml; lineNumber: 4; columnNumber: 3; The element type \\\"file\\\" must be terminated by the matching end-tag \\\"</file>\\\".\"}]}";

    private static final String EXPECTED_INVALID_NAME = "{\"tool\":null,\"issues\":[{\"filePath\":\"invalid_name.xml\",\"startLine\":1,\"rule\":\"ExceptionDuringParsing\",\"category\":\"miscellaneous\",\"message\":\"An exception occurred during parsing the report for file invalid_name.xml. Exception: de.tum.in.ase.parser.exception.UnsupportedToolException: Tool for identifying tag data not found\"}]}";

    private void testParser(String fileName, String expected) throws ParserException {
        File file = new File(fileName);
        ReportParser parser = new ReportParser();
        String actual = parser.transformToJSONReport(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckstyleParser() throws ParserException {
        testParser("src/test/java/checkstyle-result.xml", EXPECTED_CHECKSTYLE);
    }

    @Test
    public void testPMDCPDParser() throws ParserException {
        testParser("src/test/java/cpd.xml", EXPECTED_PMD_CPD);
    }

    @Test
    public void testPMDParser() throws ParserException {
        testParser("src/test/java/pmd.xml", EXPECTED_PMD);
    }

    @Test
    public void testSpotbugsParser() throws ParserException {
        testParser("src/test/java/spotbugsXml.xml", EXPECTED_SPOTBUGS);
    }

    @Test
    public void testSwiftlintParser() throws ParserException {
        testParser("src/test/java/swiftlint-result.xml", EXPECTED_SWIFTLINT);
    }

    @Test
    public void testParseInvalidFilename() throws ParserException {
        testParser("src/test/java/cpd_invalid.txt", EXPECTED_INVALID_FILENAME);
    }

    @Test
    public void testParseInvalidXML() throws ParserException {
        File file = new File("src/test/java/invalid_xml.xml");
        ReportParser parser = new ReportParser();
        String actual = parser.transformToJSONReport(file);
        if (!actual.equals(EXPECTED_INVALID_XML_GER)) {
            assertEquals(actual, EXPECTED_INVALID_XML_ENG);
        }
    }

    @Test
    public void testInvalidName() throws ParserException {
        testParser("src/test/java/invalid_name.xml", EXPECTED_INVALID_NAME);
    }
}
