package de.tum.in.ase.parser.strategy;

import java.util.Objects;
import java.util.Optional;

public enum StaticCodeAnalysisTool {
    SPOTBUGS("BugCollection", new SpotbugsParser()),
    CHECKSTYLE("checkstyle", new CheckstyleParser()),
    PMD("pmd", new PMDParser()),
    PMD_CPD("pmd-cpd", new PMDCPDParser());

    private String identifierTag;
    private ParserStrategy strategy;

    StaticCodeAnalysisTool(String identifyingTag, ParserStrategy strategy) {
        this.identifierTag = identifyingTag;
        this.strategy = strategy;
    }

    public String getIdentifyingTag() {
        return this.identifierTag;
    }

    public ParserStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Finds a tool by its identifying tag (unique element name for the specific report)
     *
     * @param identifierTag tag to search for
     * @return Optional with the corresponding tool or empty optional if no appropriate tool was found
     */
    public static Optional<StaticCodeAnalysisTool> getToolByIdentifierTag(String identifierTag) {
        for (StaticCodeAnalysisTool tool : StaticCodeAnalysisTool.values()) {
            if (Objects.equals(identifierTag, tool.getIdentifyingTag())) {
                return Optional.of(tool);
            }
        }
        return Optional.empty();
    }
}
