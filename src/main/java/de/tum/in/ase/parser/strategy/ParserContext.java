package de.tum.in.ase.parser.strategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import de.tum.in.ase.parser.exception.UnsupportedToolException;
import de.tum.in.ase.parser.utils.FileUtils;

public class ParserContext {

    // Reports that are bigger then the threshold will not be parsed
    // and an issue will be generated. The unit is in bytes.
    private final int staticCodeAnalysisReportFilesizeLimit = 1;

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
        if (FileUtils.isFilesizeGreaterThan(file, staticCodeAnalysisReportFilesizeLimit)) {
            return createFileTooLargeReport(file.getName());
        }

        Builder parser = new Builder();
        Document doc = parser.build(file);
        return parseDocument(doc);
    }

    private Report parseDocument(Document doc) {
        ParserPolicy parserPolicy = new ParserPolicy();
        ParserStrategy parserStrategy = parserPolicy.configure(doc);
        return parserStrategy.parse(doc);
    }

    /**
     * Creates a report which states that the specified file is too large
     * to be parsed by the parser.
     *
     * @param filename The name of the file
     * @return The report.
     */
    private Report createFileTooLargeReport(String filename) {
        StaticCodeAnalysisTool tool = StaticCodeAnalysisTool.getToolByFilename(filename).orElseGet(null);
        Report report = new Report(tool);

        Issue issue = new Issue();
        issue.setCategory("miscellaneous");
        issue.setMessage(String.format("There are too many issues found in the %s tool.", tool.toString()));
        issue.setFilePath(filename);
        issue.setStartLine(1);
        issue.setRule("TooManyIssues");

        ArrayList<Issue> issues = new ArrayList<>();
        issues.add(issue);
        report.setIssues(issues);
        return report;
    }
}
