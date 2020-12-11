package de.tum.in.ase.parser.strategy;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Document;
import nu.xom.Element;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;

public abstract class CheckstyleFormatParser implements ParserStrategy {

    protected static final String FILE_TAG = "file";
    protected static final String FILE_ATT_NAME = "name";
    protected static final String ERROR_ATT_SOURCE = "source";
    protected static final String ERROR_ATT_SEVERITY = "severity";
    protected static final String ERROR_ATT_MESSAGE = "message";
    protected static final String ERROR_ATT_LINENUMBER = "line";
    protected static final String ERROR_ATT_COLUMN = "column";

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

    protected abstract void extractRuleAndCategory(Issue issue, String errorSource);
}
