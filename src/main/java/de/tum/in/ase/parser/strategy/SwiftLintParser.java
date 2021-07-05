package de.tum.in.ase.parser.strategy;

import org.w3c.dom.Document;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;

class SwiftLintParser extends CheckstyleFormatParser {

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.SWIFTLINT);
        extractIssues(doc, report);
        return report;
    }

    /**
     * Extracts and sets the rule and the category given the check's package name.
     *
     * @param issue issue under construction
     * @param errorSource package like swiftlint.rules.trailing_semicolon
     */
    @Override
    protected void extractRuleAndCategory(Issue issue, String errorSource) {
        String[] errorSourceSegments = errorSource.split("\\.");
        int noOfSegments = errorSourceSegments.length;
        String rule = errorSourceSegments[noOfSegments - 1]; // e.g. trailing_semicolon
        String category = "swiftLint";

        issue.setRule(rule);
        issue.setCategory(category);
    }
}
