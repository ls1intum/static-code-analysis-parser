package de.tum.in.ase.parser.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Issue {

    public static final int MAX_STATIC_CODE_ANALYSIS_MESSAGE_LENGTH = 4500;

    // Path to the source file with unix file separators
    private String filePath;

    /**
     * Usage of line and column attributes by tools:
     * Spotbugs -> startLine, endLine (duplicated startLine)
     * Checkstyle -> startLine, endLine (duplicated startLine), startColumn, endColumn (duplicated startColumn)
     * PMD -> startLine, endLine, startColumn, endColumn
     */
    private Integer startLine;

    private Integer endLine;

    private Integer startColumn;

    private Integer endColumn;

    private String rule;

    private String category;

    private String message;

    // TODO: This is currently not used in Artemis but could be useful for further filtering.
    // Map tool specific codes to a common format
    private String priority;

    public Issue() {
    }

    public Issue(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
    }

    public Integer getEndLine() {
        return endLine;
    }

    public void setEndLine(Integer endLine) {
        this.endLine = endLine;
    }

    public Integer getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(Integer startColumn) {
        this.startColumn = startColumn;
    }

    public Integer getEndColumn() {
        return endColumn;
    }

    public void setEndColumn(Integer endColumn) {
        this.endColumn = endColumn;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null || message.length() <= MAX_STATIC_CODE_ANALYSIS_MESSAGE_LENGTH) {
            this.message = message;
        }
        else {
            this.message = message.substring(0, MAX_STATIC_CODE_ANALYSIS_MESSAGE_LENGTH);
        }
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Issue issue)) {
            return false;
        }

        return Objects.equals(filePath, issue.filePath)
                && Objects.equals(startLine, issue.startLine)
                && Objects.equals(endLine, issue.endLine)
                && Objects.equals(startColumn, issue.startColumn)
                && Objects.equals(endColumn, issue.endColumn)
                && Objects.equals(rule, issue.rule)
                && Objects.equals(category, issue.category)
                && Objects.equals(message, issue.message)
                && Objects.equals(priority, issue.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath, startLine, endLine, startColumn, endColumn, rule, category, message, priority);
    }

    @Override
    public String toString() {
        return "Issue{" +
                "filePath='" + filePath + '\'' +
                ", startLine=" + startLine +
                ", endLine=" + endLine +
                ", startColumn=" + startColumn +
                ", endColumn=" + endColumn +
                ", rule='" + rule + '\'' +
                ", category='" + category + '\'' +
                ", message='" + message + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }
}
