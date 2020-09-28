package de.tum.in.www1.staticCodeAnalysisParser.strategy;

import de.tum.in.www1.staticCodeAnalysisParser.domain.Report;
import de.tum.in.www1.staticCodeAnalysisParser.exception.UnsupportedToolException;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import java.io.File;
import java.io.IOException;

public class ParserContext {

    private ParserStrategy parserStrategy;
    private ParserPolicy parserPolicy = new ParserPolicy(this);

    void setParserStrategy(ParserStrategy parserStrategy) {
        this.parserStrategy = parserStrategy;
    }

    public Report getReport(File file, String tool) throws UnsupportedToolException, ParsingException, IOException {
        // Configure the strategy given the name of the tool
        parserPolicy.configure(tool);

        // Build the DOM and parse the document using the configured strategy
        Builder parser = new Builder();
        Document doc = parser.build(file);
        return parserStrategy.parse(doc);
    }
}
