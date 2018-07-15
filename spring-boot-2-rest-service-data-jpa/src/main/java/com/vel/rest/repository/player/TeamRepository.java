package com.vel.rest.repository.player;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.player.Team;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

	Team findByPlayers(long playerId);
}
