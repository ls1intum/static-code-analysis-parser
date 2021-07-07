package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import com.google.json.JsonSanitizer;

import java.util.ArrayList;
import java.util.List;

public class GCCParser implements ParserStrategy {
    protected static final String FILE_ATT_NAME = "file";
    protected static final String ERROR_ATT_MESSAGE = "message"; // Maybe verbose is also an option;
    protected static final String ERROR_ATT_LINENUMBER = "line";
    protected static final String ERROR_ATT_COLUMN = "column";
    protected static final String ERROR_ATT_CARET = "caret";
    protected static final String ERROR_ATT_KIND = "kind";
    protected static final String ERROR_ATT_LOCATIONS = "locations";
    protected static final String ERROR_ATT_OPTION = "option";
    protected static final int CARET_POS = 0;

    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.GCC);
        extractIssues(doc, report);
        return report;
    }

    /**
     * Constructs issues and adds them to the static analysis report.
     *
     * @param doc Contains the json output from GCC
     * @param report The report the issues will be added to
     */
    protected void extractIssues(Document doc, Report report) {
        String jsonInput = JsonSanitizer.sanitize(doc.getDocumentElement().getNodeValue());

        JSONArray gccIssues = new JSONArray(jsonInput);

        List<Issue> issues = new ArrayList<>();

        // Iterate over all array entries
        for (int i = 0; i < gccIssues.length(); i++) {
            JSONObject error = gccIssues.getJSONObject(i);
            JSONArray locations = error.getJSONArray(ERROR_ATT_LOCATIONS);
            JSONObject info = locations.getJSONObject(CARET_POS).getJSONObject(ERROR_ATT_CARET);

            String unixPath = ParserUtils.transformToUnixPath(info.getString(FILE_ATT_NAME));

            Issue issue = new Issue(unixPath);

            String message = error.getString(ERROR_ATT_MESSAGE);
            String option = error.getString(ERROR_ATT_OPTION); // Gives back the kind of error or warning e.g. -Wmisleading-indentation

            issue.setMessage(message);
            issue.setCategory(option);
            issue.setRule(option);

            // We do not distinguish between start line and end line, same for the columns
            int startLine = info.getInt(ERROR_ATT_LINENUMBER);
            issue.setStartLine(startLine);
            issue.setEndLine(startLine);

            int startColumn = info.getInt(ERROR_ATT_COLUMN);
            issue.setStartColumn(startColumn);
            issue.setEndColumn(startColumn);

            String type = error.getString(ERROR_ATT_KIND);
            issue.setPriority(type);
            issue.setCategory(type);

            issues.add(issue);
        }
        report.setIssues(issues);
    }
}
