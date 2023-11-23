package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.google.common.base.Objects;

public class Acquisition extends BaseObject {
    private String content;
    private String medium;
    private String source;
    private String term;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(final String medium) {
        this.medium = medium;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(final String term) {
        this.term = term;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Acquisition)) return false;
        if (!super.equals(o)) return false;
        final Acquisition that = (Acquisition) o;
        return Objects.equal(getContent(), that.getContent()) && Objects.equal(getMedium(), that.getMedium()) && Objects.equal(getSource(), that.getSource()) && Objects.equal(getTerm(), that.getTerm());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), getContent(), getMedium(), getSource(), getTerm());
    }

    @Override
    public String toString() {
        return "Acquisition{" +
                "content='" + content + '\'' +
                ", medium='" + medium + '\'' +
                ", source='" + source + '\'' +
                ", term='" + term + '\'' +
                "} " + super.toString();
    }
}
