package de.tum.in.ase.parser.domain;

import java.util.List;
import java.util.Objects;

import de.tum.in.ase.parser.strategy.StaticCodeAnalysisTool;

public class Report {

    private StaticCodeAnalysisTool tool;

    private List<Issue> issues;

    Report() {
        // default constructor required for Jackson deserialisation
    }

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Report report)) {
            return false;
        }
        return tool == report.tool && Objects.equals(issues, report.issues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tool, issues);
    }

    @Override
    public String toString() {
        return "Report{tool=" + tool + ", issues=" + issues + '}';
    }
}
