package com.vel.rest.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vel.rest.domain.player.Player;
import com.vel.rest.domain.player.Team;
import com.vel.rest.repository.player.PlayerRepository;
import com.vel.rest.repository.player.TeamRepository;
import com.vel.rest.service.SoccerService;

@Service
public class SoccerServiceImpl implements SoccerService {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public List<String> getAllTeamPlayers(long teamId) {
		List<String> result = new ArrayList<String>();
		List<Player> players = playerRepository.findByTeamId(teamId);
		for (Player player : players) {
			result.add(player.getName());
		}

		return result;
	}

	@Override
	public void addBarcelonaPlayer(String name, String position, int number) {

		Optional<Team> barcelona = teamRepository.findById(1l);
		Player newPlayer = new Player();
		newPlayer.setName(name);
		newPlayer.setPosition(position);
		newPlayer.setNum(number);
		newPlayer.setTeam(barcelona.get());
		playerRepository.save(newPlayer);
	}

}
