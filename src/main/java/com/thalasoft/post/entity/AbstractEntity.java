package com.thalasoft.post.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AbstractEntity)) {
            return false;
        }

        AbstractEntity other = (AbstractEntity) o;

        return id != null && id.equals(other.getId());
    }

    // The equals() and hashCode() methods must behave consistently across all state
    // transitions of the entity
    // The hash code must remain the same accross all states of the persistence life
    // cycle
    // Thus the object address cannot be the hash code as a new object is created
    // after the entity is persisted
    // And the entity id cannot be the hash code as an entity not yet persisted has
    // no id and gets one after being persisted
    // As the hash code should always return the same value, the class name is used
    // as the hash code
    // See
    // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}