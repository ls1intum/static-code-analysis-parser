package de.tum.in.ase.parser;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tum.in.ase.parser.domain.Report;
import de.tum.in.ase.parser.exception.ParserException;
import de.tum.in.ase.parser.strategy.ParserContext;
import de.tum.in.ase.parser.utils.FileUtils;

/**
 * Public API for parsing of static code analysis reports
 */
public class ReportParser {

    /**
     * Transform a given static code analysis report into a JSON representation.
     * All supported tools share the same JSON format.
     *
     * @param file Reference to the static code analysis report
     * @return Static code analysis report represented as a JSON String
     * @throws ParserException - If any error occurs parsing the report
     */
    public String transformToJSONReport(File file) throws ParserException {
        try {
            if (file == null) {
                throw new IllegalArgumentException("File must not be null");
            }

            // The static code analysis parser only supports xml files.
            if (!(FileUtils.getExtension(file).equals("xml"))) {
                throw new IllegalArgumentException("File must be xml or json format");
            }

            ParserContext context = new ParserContext();
            Report report = context.getReport(file);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(report);
        }
        catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }
    }

    /**
     * Transform a given static code analysis report given as a file into a plain Java object.
     *
     * @param file Reference to the static code analysis report
     * @return Static code analysis report represented as a plain Java object
     * @throws ParserException - If any error occurs parsing the report
     */
    public Report transformToReport(File file) throws ParserException {
        try {
            if (file == null) {
                throw new IllegalArgumentException("File must not be null");
            }

            // The static code analysis parser only supports xml files.
            if (!(FileUtils.getExtension(file).equals("xml") || FileUtils.getExtension(file).equals("xml"))) {
                throw new IllegalArgumentException("File must be xml or json format");
            }

            ParserContext context = new ParserContext();

            return context.getReport(file);
        }
        catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
