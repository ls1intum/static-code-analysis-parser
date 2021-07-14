package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.tum.in.ase.parser.utils.XmlUtils.getChildElements;
import static de.tum.in.ase.parser.utils.XmlUtils.getFirstChild;

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

        Element cppCheckLog = doc.getDocumentElement();

        Optional<Element> errors = getFirstChild(cppCheckLog, "errors");

        if (!errors.isPresent()) {
            throw new IllegalArgumentException("Not a valid error format!");
        }

        // TODO: Decide where to put all the elements for maximum readability on Artemis
        for (Element errorElement : getChildElements(errors.get(), ERROR_TAG)) {
            String unixPath = ParserUtils.transformToUnixPath(errorElement.getAttribute(FILE_ATT_NAME));

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

            // Filter out faulty error
            if (!issue.getRule().equals("missingIncludeSystem")) {
                issues.add(issue);
            }
        }

        report.setIssues(issues);
    }
}
