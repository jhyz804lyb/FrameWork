package com.jh.service;

import java.util.Date;
import java.util.List;

import com.jh.base.BaseDao;
import com.jh.entity.Match;

public interface MatchService extends BaseDao<Match>{
	
  public void saveList(List<Match> lists);
  
  public List<Match> getAllMatchList();
  
  public List<Match> getMatchByTime(Date time);
  
  public Match getMatchByMId(Integer mid);
  
  public void saveEntity(Object obj, Class classname);
}
