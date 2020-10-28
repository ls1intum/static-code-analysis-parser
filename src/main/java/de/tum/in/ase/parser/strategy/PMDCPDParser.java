package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import nu.xom.Document;
import nu.xom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class PMDCPDParser implements ParserStrategy {

    // Category/Rule which can be used by clients for further processing
    private static final String CPD_CATEGORY = "Copy/Paste Detection";

    private static final String DUPLICATION_TAG = "duplication";
    private static final String DUPLICATION_ATT_LINES = "lines";
    private static final String FILE_TAG = "file";
    private static final String FILE_ATT_PATH = "path";
    private static final String FILE_ATT_STARTLINE = "line";
    private static final String FILE_ATT_ENDLINE = "endline";
    private static final String FILE_ATT_STARTCOLUMN = "column";
    private static final String FILE_ATT_ENDCOLUMN = "endcolumn";

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.PMD);
        List<Issue> allIssues = new ArrayList<>();
        Element root = doc.getRootElement();

        // Iterate over all <duplication> elements
        for (Element duplication : root.getChildElements(DUPLICATION_TAG)) {
            List<Issue> issuesForDuplication = new ArrayList<>();
            int lines = ParserUtils.extractInt(duplication, DUPLICATION_ATT_LINES);

            // Create an issue for each found duplication
            for (Element file : duplication.getChildElements(FILE_TAG, duplication.getNamespaceURI())) {
                Issue issue = new Issue();
                issue.setCategory(CPD_CATEGORY);
                issue.setRule(CPD_CATEGORY);
                String unixPath = ParserUtils.transformToUnixPath(file.getAttributeValue(FILE_ATT_PATH));
                issue.setFilePath(unixPath);
                issue.setStartLine(ParserUtils.extractInt(file, FILE_ATT_STARTLINE));
                issue.setEndLine(ParserUtils.extractInt(file, FILE_ATT_ENDLINE));
                issue.setStartColumn(ParserUtils.extractInt(file, FILE_ATT_STARTCOLUMN));
                issue.setEndColumn(ParserUtils.extractInt(file, FILE_ATT_ENDCOLUMN));

                issuesForDuplication.add(issue);
            }

            // Create a message referencing all locations where the same duplication was found
            String message = createMessage(lines, issuesForDuplication);
            issuesForDuplication.forEach(issue -> issue.setMessage(message));

            // Add issues to report
            allIssues.addAll(issuesForDuplication);
        }
        report.setIssues(allIssues);
        return report;
    }

    /**
     * Creates a message showing the file locations of the duplication.
     *
     * @param duplicatedLines duplicated number of lines
     * @param issues issues created for the same duplication
     * @return duplication message
     */
    private String createMessage(int duplicatedLines, List<Issue> issues) {
        StringBuilder builder = new StringBuilder();
        builder.append("Code duplication of ").append(duplicatedLines).append(" lines in the following files:");

        int counter = 1;
        // Add all file locations of the same duplication
        for (Issue issue : issues) {
            String filename = extractFilename(issue.getFilePath());
            builder.append("\n").append(counter).append(". ").append(filename).append(":");
            builder.append(issue.getStartLine()).append("-").append(issue.getEndLine());
            counter++;
        }
        return builder.toString();
    }

    private String extractFilename(String unixPath) {
        return new File(unixPath).getName();
    }
}
