package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;

class CheckstyleParser extends CheckstyleFormatParser {

    // The packages rooted at checks denote the category and rule
    private static final String CATEGORY_DELIMITER = "checks";
    // Some rules don't belong to a category. We group them under this identifier.
    private static final String CATEGORY_MISCELLANEOUS = "miscellaneous";

    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.CHECKSTYLE);
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
        String rule = errorSourceSegments[noOfSegments - 1];
        String category = errorSourceSegments[noOfSegments - 2];

        // Check if the rule has a category
        if (category.equals(CATEGORY_DELIMITER)) {
            category = CATEGORY_MISCELLANEOUS;
        }
        issue.setRule(rule);
        issue.setCategory(category);
    }
}
