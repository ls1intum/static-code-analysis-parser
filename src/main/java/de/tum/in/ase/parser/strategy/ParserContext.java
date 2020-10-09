package de.tum.in.ase.parser.strategy;

import java.io.File;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import de.tum.in.ase.parser.domain.Report;
import de.tum.in.ase.parser.exception.UnsupportedToolException;

public class ParserContext {

    private ParserStrategy parserStrategy;
    private ParserPolicy parserPolicy = new ParserPolicy(this);

    public void setParserStrategy(ParserStrategy parserStrategy) {
        this.parserStrategy = parserStrategy;
    }

    public Report getReport(File file) throws UnsupportedToolException, ParsingException, IOException {
        // Build the DOM and parse the document using the configured strategy
        Builder parser = new Builder();
        Document doc = parser.build(file);
        parserPolicy.configure(doc);
        return parserStrategy.parse(doc);
    }
}
