package de.tum.in.ase.parser.strategy;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

class PMDCPDParser implements ParserStrategy {

    private static final String PMD_CPD_RULE = "PMD CPD";

    private static final String DUPLICATION_TAG = "duplication";
    private static final String DUPLICATION_ATT_LINES = "lines";
    private static final String DUPLICATION_ATT_TOKENS = "tokens";
    private static final String CODEFRAGMENT_TAG = "codefragment";
    private static final String FILE_TAG = "file";
    private static final String FILE_ATT_PATH = "path";
    private static final String FILE_ATT_STARTLINE = "line";
    private static final String FILE_ATT_ENDLINE = "endline";
    private static final String FILE_ATT_STARTCOLUMN = "column";
    private static final String FILE_ATT_ENDCOLUMN = "endcolumn";

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.PMD);
        List<Issue> issues = new ArrayList<>();
        Element root = doc.getRootElement();

        // Iterate over all <duplication> elements
        for (Element duplication : root.getChildElements(DUPLICATION_TAG)) {
            int lines = ParserUtils.extractInt(duplication, DUPLICATION_ATT_LINES);
            int tokens = ParserUtils.extractInt(duplication, DUPLICATION_ATT_TOKENS);

            Elements codeFragments = duplication.getChildElements(CODEFRAGMENT_TAG);
            String duplicatedCode = codeFragments.size() > 0 ? codeFragments.get(0).getValue() : "";
            String hash = createReadableHash(duplicatedCode);

            // The hash of the code duplication enables the grouping of
            String message = createMessage(lines, tokens, hash);

            // Create an issue for each found duplication
            for (Element file : duplication.getChildElements(FILE_TAG, duplication.getNamespaceURI())) {
                Issue issue = new Issue();
                issue.setCategory(Constants.CPD_CATEGORY);
                issue.setRule(PMD_CPD_RULE);
                String unixPath = ParserUtils.transformToUnixPath(file.getAttributeValue(FILE_ATT_PATH));
                issue.setFilePath(unixPath);
                issue.setStartLine(ParserUtils.extractInt(file, FILE_ATT_STARTLINE));
                issue.setEndLine(ParserUtils.extractInt(file, FILE_ATT_ENDLINE));
                issue.setStartColumn(ParserUtils.extractInt(file, FILE_ATT_STARTCOLUMN));
                issue.setEndColumn(ParserUtils.extractInt(file, FILE_ATT_ENDCOLUMN));
                issue.setMessage(message);
            }
        }
        report.setIssues(issues);
        return report;
    }

    /**
     * Creates a MD5 hash of a String input and transforms it into a human readable ASCII characters using Base64.
     * If the MD5 algorithm is not available, the hash will be created by the {@link String#hashCode()}}.
     *
     * @param input String to be hashed
     * @return hashed String in human readable form
     */
    private String createReadableHash(String input) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().withoutPadding().encodeToString(digest);
        }
        catch (NoSuchAlgorithmException e) {
            // Fallback
            return String.valueOf(input.hashCode());
        }
    }

    private String createMessage(int duplicatedLines, int duplicatedTokens, String duplicationHash) {
        return "Code duplication (" + duplicationHash + ") of " + duplicatedLines + " lines (" + duplicatedTokens + " tokens) detected.";
    }
}
