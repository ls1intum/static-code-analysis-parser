package de.tum.in.ase.parser.strategy;

import nu.xom.Document;

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
     * @param errorSource package like com.puppycrawl.tools.checkstyle.checks.javadoc.JavadocPackageCheck. The first
     *                    segment after '.checks.' denotes the category and the segment after the rule. Some rules do
     *                    not belong to a category e.g. com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck.
     *                    Such rule will be grouped under {@link #CATEGORY_MISCELLANEOUS}.
     */
    @Override
    protected void extractRuleAndCategory(Issue issue, String errorSource) {
        String[] errorSourceSegments = errorSource.split("\\.");
        int noOfSegments = errorSourceSegments.length;

        // Should never happen but check for robustness
        if (noOfSegments < 2) {
            issue.setCategory(errorSource);
            return;
        }

        String rule = errorSourceSegments[noOfSegments - 1]; // e.g. trailing_semicolon
        String category = errorSourceSegments[noOfSegments - 1].split("_")[0]; // e.g. trailing

        issue.setRule(rule);
        issue.setCategory(category);
    }
}
