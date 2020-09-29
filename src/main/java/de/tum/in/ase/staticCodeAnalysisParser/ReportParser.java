package de.tum.in.ase.staticCodeAnalysisParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tum.in.ase.staticCodeAnalysisParser.domain.Report;
import de.tum.in.ase.staticCodeAnalysisParser.exception.ParserException;
import de.tum.in.ase.staticCodeAnalysisParser.strategy.ParserContext;

import java.io.File;

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
     * @param tool String determining to which tool the report belongs
     * @return Static code analysis report represented as a JSON String
     * @throws ParserException - If any error occurs parsing the report
     */
    public String transformToJSONReport(File file, String tool) throws ParserException {
        try {
            if (file == null) {
                throw new IllegalArgumentException("File must not be null");
            }
            if (tool == null) {
                throw new IllegalArgumentException("Tool String must not be null");
            }

            Report report = context.getReport(file, tool);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(report);
        }
        catch (Exception e) {
            throw new ParserException(e.getMessage(), e);
        }
    }
}
