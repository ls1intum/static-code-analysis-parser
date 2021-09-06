package de.tum.in.ase.parser.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;

public class GCCParser implements ParserStrategy {

    // The message is split into two segments
    private static final int SEGMENTS_COUNT = 2;

    // Segment 0 (Header): Contains info about filename, row, column, type etc.
    private static final int HEADER_SEGMENT_POS = 0;

    // Segment 1 (Body): Body contains the trace of the error (possibly rendered with ASCII art)
    private static final int BODY_SEGMENT_POS = 1;

    // Locations in regex group
    private static final int FILE_POS = 1;

    private static final int ROW_POS = 2;

    private static final int COLUMN_POS = 3;

    private static final int TYPE_POS = 4;

    private static final int DESCRIPTION_POS = 5;

    private static final int ERROR_POS = 6;

    // Map that contains the matching category for each error
    private static final Map<String, String> categories = new HashMap<>();

    private static final String ANALYZER_PREFIX = "[-Wanalyzer";

    // Categories
    private static final String MEMORY = "Memory";

    private static final String BAD_PRACTICE = "BadPractice";

    private static final String SECURITY = "Security";

    private static final String UNDEFINED_BEHAVIOR = "UndefinedBehavior";

    private static final String MISC = "Misc"; // For various other results that are not part of the static analysis

    // Used to parse the first line of an issue which contains all the essential data
    // e.g. "ascii_table.c:7:13: warning: variable ‘arr’ set but not used [-Wunused-but-set-variable]".
    // A colon ":" is the separator symbol from GCC.
    private static final String HEADER_REGEX = "([^:^\\n]+):(\\d+):(\\d+):\\s(\\w+\\s*\\w*):\\s(.+)(\\[.+])";
    //                                           ^          ^      ^      ^                 ^      ^
    //                                           |          |      |      |                 |      |
    //                                           |          |      |      |                 |      |
    //                                           |          |      |      |                 |       +- error name eg. [-Wunused-but-set-variable]
    //                                           |          |      |      |                 +- message text e.g. warning: variable ‘arr’ set but not used
    //                                           |          |      |      +- type e.g. (error|warning|note)
    //                                           |          |      +- column e.g. 13
    //                                           |          +- row e.g. 7
    //                                           +- filename e.g. ascii_table.c

    // More generic regex similar to HEADER_REGEX, that describes the beginning of a basic GCC message, that is used to divide the output into chunks.
    // A look ahead regex (see "?=") is used, since we need to keep the delimiter after the split.
    // We need to include new line, so we can guarantee that we actually match to a new message.
    private static final String DELIM_REGEX = "(?=(\\n([^:^\\n]+):(\\d)+:(\\d)+:(.)+:(.)+))";

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.GCC);
        extractIssues(doc, report);
        return report;
    }

    /**
     * Constructs issues and adds them to the static analysis report.
     *
     * @param doc Contains the output from GCC SCA.
     * @param report The report the issues will be added to.
     */
    private void extractIssues(Document doc, Report report) {
        Element gccLog = doc.getDocumentElement();

        String[] logItems = gccLog.getTextContent().split(DELIM_REGEX);

        Pattern pattern = Pattern.compile(HEADER_REGEX);
        Matcher matcher = pattern.matcher("");

        initCategoryMapping();

        List<Issue> issues = new ArrayList<>();

        for (String entry : logItems) {

            // Chops off the leading \n which makes parsing more clear, as we avoid an empty extra segment
            String[] segments = entry.substring(1).split("\n", SEGMENTS_COUNT);

            if (segments.length < 2) {
                continue;
            }

            String header = segments[HEADER_SEGMENT_POS];
            String body = segments[BODY_SEGMENT_POS];

            matcher.reset(header);

            if (!matcher.find()) {
                continue;
            }

            // Construct issue details based on regex groups
            String filename = matcher.group(FILE_POS).trim();
            Integer row = Integer.parseInt(matcher.group(ROW_POS));
            Integer col = Integer.parseInt(matcher.group(COLUMN_POS));
            String type = matcher.group(TYPE_POS);
            String description = matcher.group(DESCRIPTION_POS);
            String warningName = matcher.group(ERROR_POS);

            // Only output warnings that have a name associated with it
            if (warningName == null) {
                continue;
            }
            // warningName is included in the description, as it will not be shown be Artemis otherwise
            String message = warningName + ": " + description + "\n" + body;

            Issue issue = new Issue(null);

            issue.setMessage(message);
            issue.setFilePath(filename);
            issue.setStartLine(row);
            issue.setEndLine(row);
            issue.setStartColumn(col);
            issue.setEndColumn(col);
            issue.setRule(warningName);
            issue.setPriority(type); // Could potentially be used for sorting at some point, not displayed by Artemis

            boolean isAnalyzerIssue = warningName.startsWith(ANALYZER_PREFIX);

            // Set correct category, only real static analysis issues are categorized, see https://gcc.gnu.org/onlinedocs/gcc-11.1.0/gcc/Static-Analyzer-Options.html
            if (isAnalyzerIssue) {
                String category = categories.get(warningName);
                issue.setCategory(category);
            }
            else {
                issue.setCategory(MISC);
            }

            issues.add(issue);
        }
        report.setIssues(issues);
    }

    private void initCategoryMapping() {
        // Memory warnings
        categories.put("[-Wanalyzer-free-of-non-heap]", MEMORY);
        categories.put("[-Wanalyzer-malloc-leak]", MEMORY);
        categories.put("[-Wanalyzer-file-leak]", MEMORY);
        categories.put("[-Wanalyzer-mismatching-deallocation]", MEMORY);

        // Undefined behavior
        categories.put("[-Wanalyzer-double-free]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-null-argument]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-use-after-free]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-use-of-uninitialized-value]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-write-to-const]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-write-to-string-literal]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-possible-null-argument]", UNDEFINED_BEHAVIOR);
        categories.put("[-Wanalyzer-possible-null-dereference]", UNDEFINED_BEHAVIOR);

        // Bad Practice
        categories.put("[-Wanalyzer-double-fclose]", BAD_PRACTICE);
        categories.put("[-Wanalyzer-too-complex]", BAD_PRACTICE);
        categories.put("[-Wanalyzer-stale-setjmp-buffer]", BAD_PRACTICE);

        // Security
        categories.put("[-Wanalyzer-exposure-through-output-file]", SECURITY);
        categories.put("[-Wanalyzer-unsafe-call-within-signal-handler]", SECURITY);
        categories.put("[-Wanalyzer-use-of-pointer-in-stale-stack-frame]", SECURITY);
        categories.put("[-Wanalyzer-tainted-array-index]", SECURITY);
    }
}
