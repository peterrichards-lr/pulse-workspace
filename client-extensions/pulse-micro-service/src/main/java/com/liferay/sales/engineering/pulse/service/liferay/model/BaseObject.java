package com.liferay.sales.engineering.pulse.service.liferay.model;

import com.google.common.base.Objects;

public abstract class BaseObject {
    private String externalReferenceCode;
    private long id;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseObject)) return false;
        final BaseObject that = (BaseObject) o;
        return getId() == that.getId() && Objects.equal(getExternalReferenceCode(), that.getExternalReferenceCode());
    }

    public String getExternalReferenceCode() {
        return externalReferenceCode;
    }

    public void setExternalReferenceCode(final String externalReferenceCode) {
        this.externalReferenceCode = externalReferenceCode;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getExternalReferenceCode());
    }

    @Override
    public String toString() {
        return "BaseObject{" +
                "id=" + id +
                ", externalReferenceCode='" + externalReferenceCode + '\'' +
                '}';
    }
}

