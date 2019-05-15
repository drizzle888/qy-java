package com.game.dao;

import java.util.List;

import com.common.entity.Score;

public interface ScoreDao {

	public void create(Score score);
	
	public void update(Score score);

	public List<Score> getByMemberId(Long memberId);
	
	public Integer getCount(Score score);
}