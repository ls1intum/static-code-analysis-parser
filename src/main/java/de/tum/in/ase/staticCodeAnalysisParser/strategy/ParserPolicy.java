package de.tum.in.ase.staticCodeAnalysisParser.strategy;

import nu.xom.Document;

import de.tum.in.ase.staticCodeAnalysisParser.exception.UnsupportedToolException;

class ParserPolicy {

    private ParserContext context;

    public ParserPolicy(ParserContext context) {
        this.context = context;
    }

    /**
     * Selects the appropriate parsing strategy by looking for the identifying tag of a static code analysis tool
     *
     * @param document static code analysis xml report
     * @throws UnsupportedToolException - If the specified tool is not supported
     */
    public void configure(Document document) {
        String rootTag = document.getRootElement().getLocalName();
        StaticCodeAnalysisTool tool = StaticCodeAnalysisTool.getToolByIdentifierTag(rootTag)
                .orElseThrow(() -> new UnsupportedToolException("Tool for identifying tag " + rootTag + " not found"));
        context.setParserStrategy(tool.getStrategy());
    }
}
