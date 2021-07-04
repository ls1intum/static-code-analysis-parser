package de.tum.in.ase.parser.strategy;

import org.w3c.dom.Document;

import de.tum.in.ase.parser.domain.Report;

interface ParserStrategy {

    /**
     * Parse a static code analysis report into a common Java representation.
     *
     * @param doc XML DOM Document
     * @return Report object containing the parsed report information
     */
    Report parse(Document doc);
}
