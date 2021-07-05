package de.tum.in.ase.parser.strategy;

import static de.tum.in.ase.parser.utils.XmlUtils.getFirstChild;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.tum.in.ase.parser.exception.UnsupportedToolException;

class ParserPolicy {

    /**
     * Selects the appropriate parsing strategy by looking for the identifying tag of a static code analysis tool
     *
     * @param document static code analysis xml report
     * @return the parser strategy
     * @throws UnsupportedToolException - If the specified tool is not supported
     */
    public ParserStrategy configure(Document document) {
        String rootTag = document.getDocumentElement().getNodeName();
        StaticCodeAnalysisTool tool = StaticCodeAnalysisTool.getToolByIdentifierTag(rootTag)
                .orElseThrow(() -> new UnsupportedToolException("Tool for identifying tag " + rootTag + " not found"));

        if (tool.getIdentifyingTag().equals("checkstyle")) {
            // Check for different checkstyle parsers
            return getCorrectCheckstyleParser(document);
        } else {
            return tool.getStrategy();
        }
    }

    /**
     * Based on the reported files which are listed within the xml document, we search for the used programming language
     * and set the parser accordingly
     *
     * @param document static code analysis xml report
     * @return the parser strategy
     */
    private ParserStrategy getCorrectCheckstyleParser(Document document) {
        Element root = document.getDocumentElement();
        return getFirstChild(root, CheckstyleFormatParser.FILE_TAG).map(fileElement -> {
            String nameValue = fileElement.getAttribute(CheckstyleFormatParser.FILE_ATT_NAME);
            String unixPath = ParserUtils.transformToUnixPath(nameValue);
            return CheckstyleFormatParser.getProgrammingLanguage(unixPath);
        }).map(language -> {
            if (language.equals("swift")) {
                return StaticCodeAnalysisTool.SWIFTLINT.getStrategy();
            } else {
                return StaticCodeAnalysisTool.CHECKSTYLE.getStrategy();
            }
            // default to normal checkstyle tool
        }).orElse(StaticCodeAnalysisTool.CHECKSTYLE.getStrategy());
    }
}
