package de.tum.in.ase.parser.strategy;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

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
        String rootTag = document.getRootElement().getLocalName();
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
        Element root = document.getRootElement();
        String language;
        Elements fileElements = root.getChildElements(CheckstyleFormatParser.FILE_TAG);
        if (fileElements.size() > 0) {
            Element fileElement = fileElements.get(0);
            String unixPath = ParserUtils.transformToUnixPath(fileElement.getAttributeValue(CheckstyleFormatParser.FILE_ATT_NAME));
            language = CheckstyleFormatParser.getProgrammingLanguage(unixPath);
            if (language.equals("swift")) {
                return StaticCodeAnalysisTool.SWIFTLINT.getStrategy();
            } else {
                return StaticCodeAnalysisTool.CHECKSTYLE.getStrategy();
            }
        } else {
            // default checkstyle tool
            return StaticCodeAnalysisTool.CHECKSTYLE.getStrategy();
        }
    }
}
