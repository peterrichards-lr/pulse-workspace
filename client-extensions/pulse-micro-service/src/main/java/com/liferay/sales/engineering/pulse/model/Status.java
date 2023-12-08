package com.liferay.sales.engineering.pulse.model;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Status {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Status() {
    }

    public Status(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;
        final Status status = (Status) o;
        return Objects.equal(id, status.id) && Objects.equal(name, status.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getName());
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
