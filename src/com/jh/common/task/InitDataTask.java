package com.jh.common.task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jh.common.Cost;
import com.jh.entity.CountType;
import com.jh.entity.Match;
import com.jh.entity.MatchCount;
import com.jh.service.MatchService;
import com.jh.utils.DataInit;

@Service
public class InitDataTask implements InitTask {
	@Resource
	MatchService matchService;

	@Override
	// @Scheduled(cron="0 0 0 * * ? ") //???24????
	// @Scheduled(cron ="0/5 * *  * * ? " )// ?????????2??2:55????????6??6:55?????5???????
	public void initData() throws ParseException, IOException {
		System.out.println("???????????------------------");
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		if (Cost.lastInitTime != null
				&& Cost.lastInitTime.equals(sd.format(new Date())))
			return;
		Cost.lastInitTime = sd.format(new Date());
		System.out.println("??????????");
		Long today = sd.parse(sd.format(new Date())).getTime();
		String data = "20150224";
		Date old = sd.parse(data);
		Long time = old.getTime() + (24 * 60 * 60 * 1000);
		// ???????
		for (int i = 0; true; i++) {

			if (time >= today)
				break;
			Date now = new Date(time);
			data = sd.format(now);
			String url = "http://bf.win007.com/football/hg/Over_" + data
					+ ".htm";
			org.jsoup.Connection c = Jsoup.connect(url);
			c.timeout(60000);
			Document doc = c.get();
			Element table = doc.getElementById("table_live");
			while (table == null || table.getElementsByTag("tr") == null
					|| table.getElementsByTag("tr").size() < 20) {
				System.out.println(url);
				org.jsoup.Connection d = Jsoup.connect(url);
				d.timeout(60000);
				doc = d.get();
				table = doc.getElementById("table_live");
			}
			List<Match> t = DataInit.getEntity(table, data);
			t = DataInit.removeExzit(t);
			matchService.saveList(t);
			time = time + (24 * 60 * 60 * 1000);
		}
	}

	//@Scheduled(cron ="0/5 * *  * * ? " )
	public void initTodayData() throws Exception {
		String sql = "select * from match_all order by match_Time desc limit 1,1";
		List<Match> m = matchService.getListBySQL(sql, Match.class, null, true);
		Match match = m.get(0);
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		Long today = sd.parse(sd.format(new Date())).getTime();
		String data = sd.format(match.getMatchTime());
		Date old = sd.parse(data);
		Long time = old.getTime() + (24 * 60 * 60 * 1000);
		for (int i = 0; true; i++) {
			if (time > today)
				break;
			Date now = new Date(time);
			data = sd.format(now);
			String url = "http://bf.win007.com/football/hg/Over_" + data
					+ ".htm";
			org.jsoup.Connection c = Jsoup.connect(url);
			c.timeout(60000);
			Document doc = c.get();
			Element table = doc.getElementById("table_live");
			while (table == null || table.getElementsByTag("tr") == null
					|| table.getElementsByTag("tr").size() < 20) {
				System.out.println(url);
				org.jsoup.Connection d = Jsoup.connect(url);
				d.timeout(60000);
				doc = d.get();
				table = doc.getElementById("table_live");
			}
			List<Match> t = DataInit.getEntity(table, data);
			t = DataInit.removeExzit(t);
			matchService.saveList(t);
			time = time + (24 * 60 * 60 * 1000);
		}
	}

	private static int limitCount = 0;

	//@Scheduled(cron = "0/5 * *  * * ? ")
	public void initTodayMatchInfoData() throws Exception {

		String sql = "select * from match_all a where not exists(select 1 from match_count m where a.match_id = m.match_id) limit "
				+ limitCount + ",10000";
		String sql1 = "select * from count_type";
		String sql2 = "select * from macth_count";
		List<Match> m = matchService.getListBySQL(sql, Match.class, null, true);
		List<CountType> lists = matchService
				.getListBySQL(sql1, new CountType());
		for (Match match : m) {
			try {
				List<MatchCount> temp = init(match, lists);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		limitCount = limitCount + 1000;
		System.out.println(limitCount);
	}

	private List<MatchCount> init(Match Matchid, List<CountType> matchCount)
			throws Exception {
		List<MatchCount> counts = null;
		String url = "http://live.titan007.com/detail/" + Matchid.getMatchId()
				+ "cn.htm";
		org.jsoup.Connection c = Jsoup.connect(url);
		c.header("Accept", "*/*");
		c.header("Accept-Encoding", "gzip, deflate, sdch");
		c.header("Accept-Language", "zh-CN,zh;q=0.8");
		c.header("Cache-Control", "no-cache");
		c.header("Host", "www.win007.com");
		c.header("Pragma", "no-cache");
		c.header(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		c.timeout(60000);
		Document doc = c.get();
		Element table = doc.getElementById("matchData");
		int count = 0;
		while ((table == null || table.getElementsByTag("table") == null || table
				.getElementsByTag("tr").size() < 20) && count < 20) {
			count++;
			org.jsoup.Connection d = Jsoup.connect(url);
			d.timeout(60000);
			d.header("Accept", "*/*");
			d.header("Accept-Encoding", "gzip, deflate, sdch");
			d.header("Accept-Language", "zh-CN,zh;q=0.8");
			d.header("Cache-Control", "no-cache");
			d.header("Host", "www.win007.com");
			d.header("Pragma", "no-cache");
			d.header(
					"User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			doc = d.get();
			table = doc.getElementById("matchData");
		}
		if (count >= 20)
			return null;
		counts = new ArrayList<MatchCount>();
		Elements tables = table.getElementsByTag("table");
		for (Element e : tables) {
			if (e.text().indexOf("???????") != -1) {
				Elements trs = e.getElementsByTag("tr");
				for (Element tr : trs) {
					if (tr.text().indexOf("???????") != -1)
						continue;
					Elements tds = tr.getElementsByTag("td");
					MatchCount entity = new MatchCount();
					entity.setMatchId(Matchid.getMatchId());
					entity.setGuestName(Matchid.getGuestTeam());
					entity.setHomeName(Matchid.getHomeTeam());
					CountType obj = valiDataByName(matchCount,
							getElementText(tds.get(2)));
					if (obj == null) {
						obj = new CountType();
						obj.setName(getElementText(tds.get(2)));
						matchService.saveEntity(obj, null);
					}
					entity.setCountTypeId(obj.getId());
					entity.setTypeName(obj.getName());
					entity.setGuestCount(getInt(tds.get(3)));
					entity.setHomeCount(getInt(tds.get(1)));
					counts.add(entity);
					matchService.saveEntity(entity, null);
					// print(tds.get(2).text().trim());
				}
			}
		}
		return counts;
		// time = time + (24 * 60 * 60 * 1000);
	}

	private Integer getInt(Element el) {
		try {
			return Integer.parseInt(getElementText(el));
		} catch (Exception e) {

		}
		return 0;
	}

	private CountType valiDataByName(List<CountType> matchCounts, String name) {
		if (matchCounts == null || name == null)
			return null;
		for (CountType matchCount : matchCounts) {
			if (name.equals(matchCount.getName()))
				return matchCount;
		}

		return null;
	}

	public static List<String> list = new ArrayList<String>();

	private void print(String s) {
		boolean isok = false;
		for (String str : list) {
			if (str.equals(s)) {
				isok = true;
			}
		}
		if (!isok) {
			list.add(s);
			System.out.println(s);
		}
	}

	private String getElementText(Element e) {
		return e.text().trim();
	}
}
