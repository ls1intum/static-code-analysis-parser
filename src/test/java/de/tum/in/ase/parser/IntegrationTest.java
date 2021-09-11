package de.tum.in.ase.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXParseException;

import de.tum.in.ase.parser.exception.ParserException;
import de.tum.in.ase.parser.utils.XmlUtils;

/**
 * Tests each parser with an example file
 */
public class IntegrationTest {

    private final static String EXPECTED_PATH = "src/test/java/expected/";

    private final static String REPORTS_PATH = "src/test/java/reports/";

    /**
     * Compares the parsed JSON report with the expected JSON report
     * @param toolGeneratedReportFileName The name of the file contains the report as generated by the different tools
     * @param expectedJSONReportFileName  The name of the file that contains the parsed report
     * @throws ParserException If an exception occurs that is not already handled by the parser itself, e.g. caused by the json-parsing
     */
    private void testParser(String toolGeneratedReportFileName, String expectedJSONReportFileName) throws ParserException {
        File toolReport = new File(REPORTS_PATH + toolGeneratedReportFileName);

        ReportParser parser = new ReportParser();
        String actual = parser.transformToJSONReport(toolReport);
        String expected = null;

        try {
            expected = Files.newBufferedReader(Path.of(EXPECTED_PATH + expectedJSONReportFileName)).readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
            Assertions.fail();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testCheckstyleParser() throws ParserException {
        testParser("checkstyle-result.xml", "checkstyle.txt");
    }

    @Test
    public void testPMDCPDParser() throws ParserException {
        testParser("cpd.xml", "pmd_cpd.txt");
    }

    @Test
    public void testPMDParser() throws ParserException {
        testParser("pmd.xml", "pmd.txt");
    }

    @Test
    public void testSpotbugsParser() throws ParserException {
        testParser("spotbugsXml.xml", "spotbugs.txt");
    }

    @Test
    public void testSwiftlintParser() throws ParserException {
        testParser("swiftlint-result.xml", "swiftlint.txt");
    }

    @Test
    public void testGCCParser() throws ParserException {
        testParser("gcc.xml", "gcc.txt");
    }

    @Test
    public void testParseInvalidFilename() throws ParserException {
        testParser("invalid_filename.xml", "invalid_filename.txt");
    }

    @Test
    public void testParseInvalidXML() throws ParserException {
        File file = new File(REPORTS_PATH + "invalid_xml.xml");
        Exception exception = assertThrows(SAXParseException.class, () -> XmlUtils.createDocumentBuilder().parse(file));

        ReportParser parser = new ReportParser();
        String expected = parser.transformToJSONReport(file);

        // JSON transform escapes quotes, so we need to escape them too
        assertEquals(expected, String.format(expected, exception.toString().replaceAll("\"", "\\\\\"")));
    }

    @Test
    public void testInvalidName() throws ParserException {
        testParser("invalid_name.xml", "invalid_name.txt");
    }
}
