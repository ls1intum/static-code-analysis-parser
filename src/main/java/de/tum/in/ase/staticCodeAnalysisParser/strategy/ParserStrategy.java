package de.tum.in.ase.staticCodeAnalysisParser.strategy;

import nu.xom.Document;

import de.tum.in.ase.staticCodeAnalysisParser.domain.Report;


interface ParserStrategy {

    /**
     * Parse a static code analysis report into a common Java representation.
     *
     * @param doc XOM DOM Document
     * @return Report object containing the parsed report information
     */
    Report parse(Document doc);
}
