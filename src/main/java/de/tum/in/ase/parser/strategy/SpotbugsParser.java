package de.tum.in.ase.parser.strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import de.tum.in.ase.parser.domain.Issue;
import de.tum.in.ase.parser.domain.Report;

class SpotbugsParser implements ParserStrategy {

    private static final String PROJECT_ELEMENT = "Project";
    private static final String SOURCE_DIRECTORY_ELEMENT = "SrcDir";
    private static final String BUGINSTANCE_ELEMENT = "BugInstance";
    private static final String BUGINSTANCE_ATT_TYPE = "type";
    private static final String BUGINSTANCE_ATT_CATEGORY = "category";
    private static final String BUGINSTANCE_ATT_PRIORITY = "priority";
    private static final String SOURCELINE_ELEMENT = "SourceLine";
    private static final String SOURCELINE_ATT_SOURCEPATH = "sourcepath";
    private static final String SOURCELINE_ATT_START = "start";
    private static final String LONGMESSAGE_ELEMENT = "LongMessage";

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.SPOTBUGS);
        List<Issue> issues = new ArrayList<>();
        // Element BugCollection
        Element root = doc.getRootElement();

        String sourceDirectory = Optional.ofNullable(root.getFirstChildElement(PROJECT_ELEMENT))
                .flatMap(p -> Optional.ofNullable(p.getFirstChildElement(SOURCE_DIRECTORY_ELEMENT)))
                .map(Element::getValue)
                .map(srcDir -> {
                    if (!srcDir.endsWith(File.separator)) {
                        return srcDir + File.separator;
                    } else {
                        return srcDir;
                    }
                }).orElse("");

        // Iterate over <BugInstance> elements
        for (Element bugInstance : root.getChildElements(BUGINSTANCE_ELEMENT)) {
            Issue issue = new Issue();

            // Extract bugInstance attributes
            issue.setRule(bugInstance.getAttributeValue(BUGINSTANCE_ATT_TYPE));
            issue.setCategory(bugInstance.getAttributeValue(BUGINSTANCE_ATT_CATEGORY));
            issue.setPriority(bugInstance.getAttributeValue(BUGINSTANCE_ATT_PRIORITY));

            // Extract information out of <SourceLine>
            Elements sourceLines = bugInstance.getChildElements(SOURCELINE_ELEMENT);
            if (sourceLines.size() > 0) {
                Element sourceLine = sourceLines.get(0);
                String unixPath = ParserUtils.transformToUnixPath(sourceDirectory + sourceLine.getAttributeValue(SOURCELINE_ATT_SOURCEPATH));
                issue.setFilePath(unixPath);
                // Set endLine by duplicating the startLine. Spotbugs does not support a endLine
                int startLine = ParserUtils.extractInt(sourceLine, SOURCELINE_ATT_START);
                issue.setStartLine(startLine);
                issue.setEndLine(startLine);
            }

            // Extract message
            Elements longMessages = bugInstance.getChildElements(LONGMESSAGE_ELEMENT);
            if (longMessages.size() > 0) {
                Element longMessage = longMessages.get(0);
                issue.setMessage(ParserUtils.stripNewLinesAndWhitespace(longMessage.getValue()));
            }
            issues.add(issue);
        }
        report.setIssues(issues);
        return report;
    }
}
