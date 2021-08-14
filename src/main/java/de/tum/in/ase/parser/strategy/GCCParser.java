package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GCCParser implements ParserStrategy {

    // Locations in regex group
    protected static final int FILE_POS = 1;
    protected static final int ROW_POS = 2;
    protected static final int COL_POS = 3;
    protected static final int TYPE_POS = 4;
    protected static final int DESC_POS = 5;
    protected static final int ERROR_POS = 6;

    // Categories
    protected static final String MEMORY = "Memory";
    protected static final String BAD_PRACTICE = "BadPractice";
    protected static final String SECURITY = "Security";
    protected static final String UNDEFINED_BEHAVIOR = "UndefinedBehavior";
    protected static final String MISC = "Misc";

    protected static final Map<String,String> categories = new HashMap<>();

    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.GCC);
        extractIssues(doc, report);
        return report;
    }

    /**
     * Constructs issues and adds them to the static analysis report.
     *
     * @param doc Contains the output from GCC SCA
     * @param report The report the issues will be added to
     */

    protected void extractIssues(Document doc, Report report) {
        initCategoryMapping();
        List<Issue> issues = new ArrayList<>();
        Element gccLog = doc.getDocumentElement();

        String delim = "(?=(\\n(.)+.\\w:(\\d)+:(\\d)+:(.)+:(.)+))";
        String[] results = gccLog.getTextContent().split(delim);

        // For simplicities' sake we use \n also as the delimiter, so we only get one actual match for each entry
        String infoEntryRegex = "([^:^\\n]+):(\\d+):(\\d+):\\s(\\w+\\s*\\w*):\\s(.+)(\\[.+\\])\\n";
        Pattern errorDetails = Pattern.compile(infoEntryRegex);
        Matcher m = errorDetails.matcher("");

        for (String entry : results) {
            Issue issue = new Issue(null);

            m.reset(entry);

            // Extract categories
            if (m.find()) {
                String filename = m.group(FILE_POS).trim();
                Integer row = Integer.parseInt(m.group(ROW_POS));
                Integer col = Integer.parseInt(m.group(COL_POS));
                String type = m.group(TYPE_POS);
                String description = m.group(DESC_POS);
                String errorName = m.group(ERROR_POS);
                String body;

                boolean isAnalyzerIssue = false;

                if (errorName != null) {
                    String[] elements = entry.split("\n", 3);
                    body = elements[2];
                    issue.setMessage(description + "\n" + body);
                    isAnalyzerIssue = errorName.startsWith("[-Wanalyzer");
                } else {
                    issue.setMessage(description);
                }

                // Set correct category, only real static analysis issues are categorized at the moment
                if (isAnalyzerIssue) {
                    issue.setCategory(categories.get(errorName));
                } else {
                    issue.setCategory(MISC);
                }

                issue.setFilePath(filename);
                issue.setStartLine(row);
                issue.setEndLine(row);
                issue.setStartColumn(col);
                issue.setEndColumn(col);
                issue.setRule(errorName);
                issue.setPriority(type); // Could potentially used by sorting at some point
                issues.add(issue);
            }
        }
        report.setIssues(issues);
    }

    protected void initCategoryMapping() {
        // Memory warnings
        categories.put("-Wanalyzer-free-of-non-heap", MEMORY);
        categories.put("-Wanalyzer-malloc-leak", MEMORY);
        categories.put("-Wanalyzer-file-leak", MEMORY);
        categories.put("-Wanalyzer-mismatching-deallocation", MEMORY);

        // Undefined behavior
        categories.put("-Wanalyzer-double-free", UNDEFINED_BEHAVIOR);
        categories.put("-Wanalyzer-null-argument", UNDEFINED_BEHAVIOR);
        categories.put("-Wanalyzer-use-after-free", UNDEFINED_BEHAVIOR);
        categories.put("-Wanalyzer-use-of-uninitialized-value", UNDEFINED_BEHAVIOR);
        categories.put("-Wanalyzer-write-to-const", UNDEFINED_BEHAVIOR);
        categories.put("-Wanalyzer-write-to-string-literal", UNDEFINED_BEHAVIOR);

        // Bad Practice
        categories.put("-Wanalyzer-possible-null-argument", BAD_PRACTICE);
        categories.put("-Wanalyzer-possible-null-dereference", BAD_PRACTICE);
        categories.put("-Wanalyzer-double-fclose", BAD_PRACTICE);
        categories.put("-Wanalyzer-too-complex", BAD_PRACTICE);
        categories.put("-Wanalyzer-stale-setjmp-buffer", BAD_PRACTICE);

        // Security
        categories.put("-Wanalyzer-exposure-through-output-file", SECURITY);
        categories.put("-Wno-analyzer-unsafe-call-within-signal-handler", SECURITY);
        categories.put("-Wanalyzer-use-of-pointer-in-stale-stack-frame", SECURITY);
        categories.put("-Wanalyzer-tainted-array-index", SECURITY);
    }
}
