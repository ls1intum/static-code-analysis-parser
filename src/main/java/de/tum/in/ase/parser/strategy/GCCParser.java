package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static de.tum.in.ase.parser.utils.XmlUtils.getChildElements;
import static de.tum.in.ase.parser.utils.XmlUtils.getFirstChild;


public class GCCParser implements ParserStrategy {
    protected static final String ERROR_TAG = "item";
    protected static final String ERROR_MESSAGE = "message";
    protected static final String ERROR_LINENUMBER = "line";
    protected static final String ERROR_COLUMN = "display-column";
    protected static final String ERROR_FILE = "file";
    protected static final String ERROR_CARET = "caret";
    protected static final String ERROR_KIND = "kind";
    protected static final String ERROR_LOCATION = "item";
    protected static final String ERROR_OPTION = "option";
    protected static final String ERROR_OPTION_URL = "option_url";

    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.GCC);
        extractIssues(doc, report);
        return report;
    }

    /**
     * Constructs issues and adds them to the static analysis report.
     *
     * @param doc Contains the convert xml output from GCC
     * @param report The report the issues will be added to
     */

    protected void extractIssues(Document doc, Report report) {
        List<Issue> issues = new ArrayList<>();

        Element gccLog = doc.getDocumentElement();

        // Iterate over all <file> elements
        for (Element item : getChildElements(gccLog, ERROR_TAG)) {

            // File path will be set later
            Issue issue = new Issue(null);

            Optional<Element> errorKind = getFirstChild(item, ERROR_KIND);
            errorKind.ifPresent(element -> issue.setPriority(element.getNodeValue()));

            Optional<Element> errorOption = getFirstChild(item, ERROR_OPTION);
            errorOption.ifPresent(element -> issue.setRule(element.getNodeValue()));

            Optional<Element> errorMessage = getFirstChild(item, ERROR_MESSAGE);
            Optional<Element> infoLink = getFirstChild(item, ERROR_OPTION_URL);

            if (errorMessage.isPresent()) {
                String linkHint;
                String message;
                linkHint = infoLink.map(element -> ". For more info see: " + element.getTextContent()).orElse("");
                message = errorMessage.map(Node::getTextContent).orElse("");
                issue.setMessage(message + linkHint);
            }

            // TODO: Support multiple locations
            Optional<Element> locations = getFirstChild(item, "locations");

            if (!locations.isPresent()) {
                continue;
            }

            // Information on the current position
            for (Element location : getChildElements(locations.get(), ERROR_LOCATION)) {

                // Caret position of the traditional GCC output
                Optional<Element> caret = getFirstChild(location, ERROR_CARET);

                if (caret.isPresent()) {
                    // Only use startLine and startColumn for now
                    Optional<Element> line = getFirstChild(caret.get(), ERROR_LINENUMBER);
                    int startLine = Integer.parseInt(line.map(Node::getTextContent).orElse("-1"));

                    issue.setStartLine(startLine);
                    issue.setEndLine(startLine);

                    Optional<Element> col = getFirstChild(caret.get(), ERROR_LINENUMBER);
                    int startColumn = Integer.parseInt(col.map(Node::getTextContent).orElse("-1"));

                    issue.setStartColumn(startColumn);
                    issue.setEndColumn(startColumn);

                    Optional<Element> errorFile = getFirstChild(caret.get(), ERROR_FILE);
                    errorFile.ifPresent(element -> issue.setFilePath(ParserUtils.transformToUnixPath(element.getTextContent())));
                }

                issues.add(issue);
            }
        }

        report.setIssues(issues);
    }
}
