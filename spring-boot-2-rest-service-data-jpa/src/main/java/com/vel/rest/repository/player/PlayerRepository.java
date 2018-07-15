package com.vel.rest.repository.player;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.player.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

	List<Player> findByTeamId(long teamId);

}
