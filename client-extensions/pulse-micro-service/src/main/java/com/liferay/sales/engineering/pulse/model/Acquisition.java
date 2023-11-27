package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;
import com.liferay.sales.engineering.pulse.util.StringUtils;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Acquisition {
    private String content;
    @Unique
    private String externalReferenceCode;
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
        this.externalReferenceCode = builder.erc;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Acquisition)) return false;
        final Acquisition that = (Acquisition) o;
        return Objects.equal(externalReferenceCode, that.externalReferenceCode) && Objects.equal(getContent(), that.getContent()) && Objects.equal(getId(), that.getId()) && Objects.equal(getMedium(), that.getMedium()) && Objects.equal(getSource(), that.getSource()) && Objects.equal(getTerm(), that.getTerm());
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getExternalReferenceCode() {
        return externalReferenceCode;
    }

    public void setExternalReferenceCode(final String externalReferenceCode) {
        this.externalReferenceCode = externalReferenceCode;
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
        return Objects.hashCode(externalReferenceCode, getContent(), getId(), getMedium(), getSource(), getTerm());
    }

    @Override
    public String toString() {
        return "Acquisition{" +
                "externalReferenceCode='" + externalReferenceCode + '\'' +
                ", content='" + content + '\'' +
                ", id=" + id +
                ", medium='" + medium + '\'' +
                ", source='" + source + '\'' +
                ", term='" + term + '\'' +
                '}';
    }

    public static class AcquisitionBuilder {
        private final String erc;
        private String content;
        private String medium;
        private String source;
        private String term;

        public AcquisitionBuilder(String erc) {
            this.erc = erc;
        }

        public Acquisition build() {

            if (StringUtils.isBlank(erc) &&
                    StringUtils.isBlank(content) &&
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
