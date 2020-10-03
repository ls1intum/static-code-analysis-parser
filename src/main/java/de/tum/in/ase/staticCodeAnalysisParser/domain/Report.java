package de.tum.in.ase.staticCodeAnalysisParser.domain;

import java.util.List;

import de.tum.in.ase.staticCodeAnalysisParser.strategy.StaticCodeAnalysisTool;

public class Report {

    private StaticCodeAnalysisTool tool;

    private List<Issue> issues;

    public Report(StaticCodeAnalysisTool tool) {
        this.tool = tool;
    }

    public StaticCodeAnalysisTool getTool() {
        return tool;
    }

    public void setTool(StaticCodeAnalysisTool tool) {
        this.tool = tool;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}
