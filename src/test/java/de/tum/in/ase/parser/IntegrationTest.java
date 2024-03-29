package de.tum.in.ase.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import de.tum.in.ase.parser.exception.ParserException;
import de.tum.in.ase.parser.utils.XmlUtils;

/**
 * Tests each parser with an example file
 */
class IntegrationTest {

    private final static Path EXPECTED_FOLDER_PATH = Paths.get("src", "test", "java", "expected");

    private final static Path REPORTS_FOLDER_PATH = Paths.get("src", "test", "java", "reports");

    /**
     * Compares the parsed JSON report with the expected JSON report
     * @param toolGeneratedReportFileName The name of the file contains the report as generated by the different tools
     * @param expectedJSONReportFileName  The name of the file that contains the parsed report
     * @throws ParserException If an exception occurs that is not already handled by the parser itself, e.g. caused by the json-parsing
     */
    private void testParserWithFile(String toolGeneratedReportFileName, String expectedJSONReportFileName) throws ParserException, IOException {
        File toolReport = REPORTS_FOLDER_PATH.resolve(toolGeneratedReportFileName).toFile();

        ReportParser parser = new ReportParser();
        String actual = parser.transformToJSONReport(toolReport);

        try (BufferedReader reader = Files.newBufferedReader(EXPECTED_FOLDER_PATH.resolve(expectedJSONReportFileName))) {
            String expected = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            assertEquals(expected, actual);
        }
    }

    /**
     * Compares the parsed JSON report with the expected JSON report
     * @param fileName The name of the file contains the report as generated by the different tools
     * @param expected The expected output
     * @throws ParserException If an exception occurs that is not already handled by the parser itself, e.g. caused by the json-parsing
     */
    private void testParserWithString(String fileName, String expected) throws ParserException {
        File toolReport = REPORTS_FOLDER_PATH.resolve(fileName).toFile();

        ReportParser parser = new ReportParser();
        String actual = parser.transformToJSONReport(toolReport);

        assertEquals(expected, actual);
    }

    @Test
    void testCheckstyleParser() throws ParserException, IOException {
        testParserWithFile("checkstyle-result.xml", "checkstyle.txt");
    }

    @Test
    void testPMDCPDParser() throws ParserException, IOException {
        testParserWithFile("cpd.xml", "pmd_cpd.txt");
    }

    @Test
    void testPMDParser() throws ParserException, IOException {
        testParserWithFile("pmd.xml", "pmd.txt");
    }

    @Test
    void testSpotbugsParser() throws ParserException, IOException {
        testParserWithFile("spotbugsXml.xml", "spotbugs.txt");
    }

    @Test
    void testSwiftlintParser() throws ParserException, IOException {
        testParserWithFile("swiftlint-result.xml", "swiftlint.txt");
    }

    @Test
    void testGCCParser() throws ParserException, IOException {
        testParserWithFile("gcc.xml", "gcc.txt");
    }

    @Test
    void testParseInvalidFilename() throws ParserException, IOException {
        testParserWithFile("cpd_invalid.txt", "invalid_filename.txt");
    }

    @Test
    void testParseInvalidXML() throws ParserException, IOException {
        Exception exception = assertThrows(SAXParseException.class,
                () -> XmlUtils.createDocumentBuilder().parse(new File(REPORTS_FOLDER_PATH.resolve("invalid_xml.xml").toString())));

        try (BufferedReader reader = Files.newBufferedReader(EXPECTED_FOLDER_PATH.resolve("invalid_xml.txt"))) {
            String expectedInvalidXML = reader.readLine();
            // JSON transform escapes quotes, so we need to escape them too
            testParserWithString("invalid_xml.xml", String.format(expectedInvalidXML, exception.toString().replaceAll("\"", "\\\\\"")));
        }
    }

    @Test
    void testInvalidName() throws ParserException, IOException {
        testParserWithFile("invalid_name.xml", "invalid_name.txt");
    }
}
