package de.tum.in.ase.staticCodeAnalysisParser.strategy;

import de.tum.in.ase.staticCodeAnalysisParser.domain.Report;
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
