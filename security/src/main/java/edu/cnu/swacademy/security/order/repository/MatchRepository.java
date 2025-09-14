package edu.cnu.swacademy.security.order.repository;

import edu.cnu.swacademy.security.order.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match,Integer> {
}
