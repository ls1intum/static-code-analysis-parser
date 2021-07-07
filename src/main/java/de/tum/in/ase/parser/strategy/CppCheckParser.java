package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static de.tum.in.ase.parser.utils.XmlUtils.getChildElements;

public class CppCheckParser implements ParserStrategy {

    protected static final String ERROR_TAG = "error";
    protected static final String ERROR_ID = "id";
    protected static final String FILE_ATT_NAME = "file";
    protected static final String ERROR_ATT_SEVERITY = "severity";
    protected static final String ERROR_ATT_MESSAGE = "msg";
    protected static final String ERROR_ATT_LINENUMBER = "line";
    protected static final String ERROR_ATT_COLUMN = "column";

    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.CPPCHECK);
        extractIssues(doc, report);
        return report;
    }

        protected void extractIssues(Document doc, Report report) {

        List<Issue> issues = new ArrayList<>();
        Element root = doc.getDocumentElement();

        // Iterate over all <file> elements
        for (Element fileElement : getChildElements(root, ERROR_TAG)) {
            String unixPath = ParserUtils.transformToUnixPath(fileElement.getAttribute(FILE_ATT_NAME));

            // Iterate over all <error> elements
            for (Element errorElement : getChildElements(fileElement)) {
                Issue issue = new Issue(unixPath);

                issue.setPriority(errorElement.getAttribute(ERROR_ATT_SEVERITY));
                issue.setCategory(errorElement.getAttribute(ERROR_ATT_SEVERITY));
                issue.setRule(errorElement.getAttribute(ERROR_ID));
                issue.setMessage(errorElement.getAttribute(ERROR_ATT_MESSAGE));

                // Set startLine as endLine as cppcheck does not support an end line
                int startLine = ParserUtils.extractInt(errorElement, ERROR_ATT_LINENUMBER);
                issue.setStartLine(startLine);
                issue.setEndLine(startLine);

                // Set startColumn as endColumn as cppcheck does not support an end column
                int startColumn = ParserUtils.extractInt(errorElement, ERROR_ATT_COLUMN);
                issue.setStartColumn(startColumn);
                issue.setEndColumn(startColumn);

                issues.add(issue);
            }
        }
        report.setIssues(issues);
    }
}
