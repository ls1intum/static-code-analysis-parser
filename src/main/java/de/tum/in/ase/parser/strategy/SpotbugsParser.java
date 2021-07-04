package de.tum.in.ase.parser.strategy;

import static de.tum.in.ase.parser.utils.XmlUtils.getChildElements;
import static de.tum.in.ase.parser.utils.XmlUtils.getFirstChild;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    private static final String SOURCELINE_ATT_END = "end";
    private static final String LONGMESSAGE_ELEMENT = "LongMessage";

    @Override
    public Report parse(Document doc) {
        Report report = new Report(StaticCodeAnalysisTool.SPOTBUGS);
        List<Issue> issues = new ArrayList<>();
        // Element BugCollection
        Element root = doc.getDocumentElement();

        String sourceDirectory = getFirstChild(root, PROJECT_ELEMENT)
                .flatMap(p -> getFirstChild(p, SOURCE_DIRECTORY_ELEMENT))
                .map(Element::getTextContent)
                .map(srcDir -> {
                    if (!srcDir.endsWith(File.separator)) {
                        return srcDir + File.separator;
                    } else {
                        return srcDir;
                    }
                }).orElse("");

        // Iterate over <BugInstance> elements
        for (Element bugInstance : getChildElements(root, BUGINSTANCE_ELEMENT)) {
            Issue issue = new Issue();

            // Extract bugInstance attributes
            issue.setRule(bugInstance.getAttribute(BUGINSTANCE_ATT_TYPE));
            issue.setCategory(bugInstance.getAttribute(BUGINSTANCE_ATT_CATEGORY));
            issue.setPriority(bugInstance.getAttribute(BUGINSTANCE_ATT_PRIORITY));

            // Extract information out of <SourceLine>
            getFirstChild(bugInstance, SOURCELINE_ELEMENT).ifPresent(sourceLine -> {
                String unixPath = ParserUtils.transformToUnixPath(sourceDirectory + sourceLine.getAttribute(SOURCELINE_ATT_SOURCEPATH));
                issue.setFilePath(unixPath);
                issue.setStartLine(ParserUtils.extractInt(sourceLine, SOURCELINE_ATT_START));
                issue.setEndLine(ParserUtils.extractInt(sourceLine, SOURCELINE_ATT_END));
            });

            getFirstChild(bugInstance, LONGMESSAGE_ELEMENT).ifPresent(longMessage -> issue.setMessage(ParserUtils.stripNewLinesAndWhitespace(longMessage.getTextContent())));
            issues.add(issue);
        }
        report.setIssues(issues);
        return report;
    }
}
