package de.tum.in.ase.parser.strategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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

    /**
     * Builds the document using the provided file and parses it to a Report object.
     *
     * @param file File referencing the static code analysis report
     * @return Report containing the static code analysis issues
     * @throws UnsupportedToolException if the static code analysis tool which created the report is not supported
     * @throws ParsingException if the document could not be built
     * @throws IOException if the file could not be read
     */
    public Report getReport(File file) throws UnsupportedToolException, ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(file);
        return parseDocument(doc);
    }

    /**
     * Builds the document using the provided inputStream and parses it to a Report object.
     *
     * @param inputStream Input stream of the static code analysis report
     * @return Report containing the static code analysis issues
     * @throws UnsupportedToolException if the static code analysis tool which created the report is not supported
     * @throws ParsingException if the document could not be built
     * @throws IOException if the inputStream could not be read
     */
    public Report getReport(InputStream inputStream) throws UnsupportedToolException, ParsingException, IOException {
        Builder parser = new Builder();
        Document doc = parser.build(inputStream);
        return parseDocument(doc);
    }

    private Report parseDocument(Document doc) {
        parserPolicy.configure(doc);
        return parserStrategy.parse(doc);
    }
}
