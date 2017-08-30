package com.jh.service.impl;

import com.jh.base.BaseDaoImlp;
import com.jh.entity.Match;
import com.jh.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchServiceimpl extends BaseDaoImlp<Match> implements
		MatchService {

	@Override
	public void saveList(List<Match> lists) {
		for (Match m : lists) {
			this.saveOrUpdate(m);
		}

	}

	@Override
	public List<Match> getAllMatchList() {
		return this.findAll();
	}

	@Override
	public List<Match> getMatchByTime(Date time) {
		return null;
	}

	@Override
	public Match getMatchByMId(Integer mid) {
		return null;
	}

	@Override
	public void saveEntity(Object obj, Class classname) {
		// TODO Auto-generated method stub
		this.getSessionFactory().getCurrentSession().save(obj);
		
	}

}
