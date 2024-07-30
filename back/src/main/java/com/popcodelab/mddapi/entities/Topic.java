package com.popcodelab.mddapi.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The Topic class represents a topic entity in the system.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
@Entity
@Table(name = "topics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Topic extends BaseEntity {

    @Column(nullable = false, length = 254)
    private String title;

    @Column(nullable = false, length = 254)
    private String description;

}
