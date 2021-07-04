package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;

interface ParserStrategy {

    /**
     * Parse a static code analysis report into a common Java representation.
     *
     * @param doc XML DOM Document
     * @return Report object containing the parsed report information
     */
    Report parse(Document doc);
}
