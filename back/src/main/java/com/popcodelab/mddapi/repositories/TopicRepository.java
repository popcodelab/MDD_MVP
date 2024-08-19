package com.popcodelab.mddapi.repositories;

import com.popcodelab.mddapi.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    /**
     * Retrieves a list of Topic entities by their IDs.
     *
     * @param ids the list of IDs of the Topic entities to retrieve
     * @return a list of Topic entities matching the provided IDs
     */
    @Query("SELECT t FROM Topic t WHERE t.id IN (:ids)")
    List<Topic> findByIds(@Param("ids") List<Long> ids);
}
