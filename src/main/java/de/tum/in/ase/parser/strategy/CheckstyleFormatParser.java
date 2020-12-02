package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import nu.xom.Document;
import nu.xom.Element;

import java.util.ArrayList;
import java.util.List;

public abstract class CheckstyleFormatParser implements ParserStrategy {

    static final String FILE_TAG = "file";
    static final String FILE_ATT_NAME = "name";
    static final String ERROR_ATT_SOURCE = "source";
    static final String ERROR_ATT_SEVERITY = "severity";
    static final String ERROR_ATT_MESSAGE = "message";
    static final String ERROR_ATT_LINENUMBER = "line";
    static final String ERROR_ATT_COLUMN = "column";

    // The packages rooted at checks denote the category and rule
    static final String CATEGORY_DELIMITER = "checks";
    // Some rules don't belong to a category. We group them under this identifier.
    static final String CATEGORY_MISCELLANEOUS = "miscellaneous";

    protected static String getProgrammingLanguage(String path) {
        String extension = path.substring(path.lastIndexOf("."));
        return extension.substring(1);
    }

    protected void extractIssues(Document doc, Report report) {
        List<Issue> issues = new ArrayList<>();
        Element root = doc.getRootElement();

        // Iterate over all <file> elements
        for (Element fileElement : root.getChildElements(FILE_TAG)) {
            String unixPath = ParserUtils.transformToUnixPath(fileElement.getAttributeValue(FILE_ATT_NAME));

            // Iterate over all <error> elements
            for (Element errorElement : fileElement.getChildElements()) {
                Issue issue = new Issue(unixPath);

                String errorSource = errorElement.getAttributeValue(ERROR_ATT_SOURCE);
                extractRuleAndCategory(issue, errorSource);

                issue.setPriority(errorElement.getAttributeValue(ERROR_ATT_SEVERITY));
                issue.setMessage(errorElement.getAttributeValue(ERROR_ATT_MESSAGE));

                // Set startLine as endLine as Checkstyle does not support an end line
                int startLine = ParserUtils.extractInt(errorElement, ERROR_ATT_LINENUMBER);
                issue.setStartLine(startLine);
                issue.setEndLine(startLine);

                // Set startColumn as endColumn as Checkstyle does not support an end column
                int startColumn = ParserUtils.extractInt(errorElement, ERROR_ATT_COLUMN);
                issue.setStartColumn(startColumn);
                issue.setEndColumn(startColumn);

                issues.add(issue);
            }
        }
        report.setIssues(issues);
    }

    abstract void extractRuleAndCategory(Issue issue, String errorSource);
}
