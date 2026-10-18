package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);


    @Query("""
        select u from User u
        left join fetch u.profile
        left join fetch u.status
        """)
    List<User> findAllJoinProfileAndStatus();

    List<User> findAllByIdIn(Collection<UUID> ids);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String s, UUID id);

    boolean existsByEmailAndIdNot(String s, UUID id);
}
