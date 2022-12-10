package de.tum.in.ase.parser.utils;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import de.tum.in.ase.parser.strategy.StaticCodeAnalysisTool;

public class ReportUtils {

    private ReportUtils() {
    }

    /**
     * Creates a report which states that the specified file is too large
     * to be parsed by the parser.
     *
     * @param filename name of the parsed file
     * @return report with the issue about the filesize
     */
    public static Report createFileTooLargeReport(String filename) {
        StaticCodeAnalysisTool tool = StaticCodeAnalysisTool.getToolByFilename(filename).orElse(null);
        Report report = new Report(tool);

        Issue issue = new Issue();
        issue.setCategory("miscellaneous");
        issue.setMessage(String.format("There are too many issues found in the %s tool.", tool));
        issue.setFilePath(filename);
        issue.setStartLine(1);
        issue.setRule("TooManyIssues");

        report.setIssues(listOf(issue));
        return report;
    }

    /**
     * Creates a report wrapping an exception; Used to inform the client about any exception during parsing
     *
     * @param filename name of the parsed file
     * @param exception exception to wrap
     * @return a report for the file with an issue wrapping the exception
     */
    public static Report createErrorReport(String filename, Exception exception) {
        StaticCodeAnalysisTool tool = StaticCodeAnalysisTool.getToolByFilename(filename).orElse(null);
        Report report = new Report(tool);

        Issue issue = new Issue();
        issue.setCategory("miscellaneous");
        issue.setMessage(String.format("An exception occurred during parsing the report for %s. Exception: %s", tool != null ? tool : "file " + filename, exception));
        issue.setFilePath(filename);
        issue.setStartLine(1);
        issue.setRule("ExceptionDuringParsing");

        report.setIssues(listOf(issue));
        return report;
    }

    /**
     * Helper to replace List.of, not available due to Java version
     * @param issue issue to wrap into a list
     * @return list containing the issue
     */
    private static List<Issue> listOf(Issue issue) {
        ArrayList<Issue> issues = new ArrayList<>();
        issues.add(issue);
        return issues;
    }
}
