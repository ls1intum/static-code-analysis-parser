package de.tum.in.www1.staticCodeAnalysisParser.strategy;

import de.tum.in.www1.staticCodeAnalysisParser.domain.Report;
import nu.xom.Document;


interface ParserStrategy {

    /**
     * Parse a static code analysis report into a common Java representation.
     *
     * @param doc XOM DOM Document
     * @return Report object containing the parsed report information
     */
    Report parse(Document doc);
}
