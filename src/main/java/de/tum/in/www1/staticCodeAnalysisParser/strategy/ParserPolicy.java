package de.tum.in.www1.staticCodeAnalysisParser.strategy;

import de.tum.in.www1.staticCodeAnalysisParser.ReportParser;
import de.tum.in.www1.staticCodeAnalysisParser.domain.StaticCodeAnalysisTool;
import de.tum.in.www1.staticCodeAnalysisParser.exception.UnsupportedToolException;

public class ParserPolicy {

    private ReportParser parser;

    public ParserPolicy(ReportParser parser) {
        this.parser = parser;
    }

    /**
     * Selects the appropriate parsing strategy.
     *
     * @param tool String identifying the static code analysis tool
     * @throws UnsupportedToolException - If the specified tool is not supported
     */
    public void configure(String tool) {
        // TODO: Inspect the document (identifying unique nodes) to select the appropriate strategy
        if (StaticCodeAnalysisTool.SPOTBUGS.name().equalsIgnoreCase(tool)) {
            parser.setParserStrategy(new SpotbugsParser());
        }
        else if (StaticCodeAnalysisTool.CHECKSTYLE.name().equalsIgnoreCase(tool)) {
            parser.setParserStrategy(new CheckstyleParser());
        }
        else if (StaticCodeAnalysisTool.PMD.name().equalsIgnoreCase(tool)) {
            parser.setParserStrategy(new PMDParser());
        }
        else {
            throw new UnsupportedToolException("Report parsing for tool " + tool + " is not supported");
        }
    }
}
