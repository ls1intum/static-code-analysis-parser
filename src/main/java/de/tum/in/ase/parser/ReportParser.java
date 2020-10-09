package de.tum.in.ase.parser;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tum.in.ase.parser.domain.Report;
import de.tum.in.ase.parser.exception.ParserException;
import de.tum.in.ase.parser.strategy.ParserContext;

/**
 * Public API for parsing of static code analysis reports
 */
public class ReportParser {

    private ParserContext context = new ParserContext();

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

            Report report = context.getReport(file);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(report);
        }
        catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
