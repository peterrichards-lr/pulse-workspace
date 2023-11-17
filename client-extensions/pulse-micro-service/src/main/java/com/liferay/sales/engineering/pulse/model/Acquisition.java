package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Acquisition {
    private String content;
    private @Id
    @GeneratedValue Long id;
    private String medium;
    private String source;
    private String term;

    public Acquisition() {
    }

    private Acquisition(AcquisitionBuilder builder) {
        this.content = builder.content;
        this.medium = builder.medium;
        this.source = builder.source;
        this.term = builder.term;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Acquisition)) return false;
        final Acquisition that = (Acquisition) o;
        return Objects.equal(getId(), that.getId()) && Objects.equal(getContent(), that.getContent()) && Objects.equal(getMedium(), that.getMedium()) && Objects.equal(getSource(), that.getSource()) && Objects.equal(getTerm(), that.getTerm());
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
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
    public int hashCode() {
        return Objects.hashCode(getId(), getContent(), getMedium(), getSource(), getTerm());
    }

    @Override
    public String toString() {
        return "Acquisition{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", medium='" + medium + '\'' +
                ", source='" + source + '\'' +
                ", term='" + term + '\'' +
                '}';
    }

    public static class AcquisitionBuilder {
        private String content;
        private String medium;
        private String source;
        private String term;

        public Acquisition build() {

            if (StringUtils.isBlank(content) &&
                    StringUtils.isBlank(medium) &&
                    StringUtils.isBlank(source) &&
                    StringUtils.isBlank(term)) {
                return null;
            }
            return new Acquisition(this);
        }

        public AcquisitionBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public AcquisitionBuilder withMedium(String medium) {
            this.medium = medium;
            return this;
        }

        public AcquisitionBuilder withSource(String source) {
            this.source = source;
            return this;
        }

        public AcquisitionBuilder withTerm(String term) {
            this.term = term;
            return this;
        }
    }
}
