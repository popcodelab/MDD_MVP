package com.popcodelab.mddapi.repositories;

import com.popcodelab.mddapi.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The TopicRepository interface is responsible for providing database operations
 * for the Topic entity in the system. It extends the JpaRepository interface, which provides
 * basic CRUD (Create, Read, Update, Delete) operations.
 * <p>
 * This interface does not provide any additional methods as it inherits all the methods
 * from its parent interface.
 * <p>
 * Note: This class does not contain any implementation code as it is an interface.
 * It serves as a contract for classes that implement it to define the actual database operations.
 *
 * @author Pignon Pierre-Olivier
 * @version 1.0
 */
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
