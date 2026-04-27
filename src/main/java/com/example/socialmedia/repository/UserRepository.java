package com.example.socialmedia.repository;

import com.example.socialmedia.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id != :userId AND u.id NOT IN (SELECT f.following.id FROM Follow f WHERE f.follower.id = :userId) AND u.verificationStatus = 'VERIFIED'")
    List<User> findSuggestedUsers(@Param("userId") Long userId, Pageable pageable);
}
