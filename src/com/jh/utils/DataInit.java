package com.jh.utils;

import com.jh.entity.Match;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.*;
import java.util.*;

public class DataInit {
	public static List<Match> removeExzit(List<Match> t) {
		List<Match> temp = new ArrayList<Match>();
		for (Match m : t) {
			boolean isOk = true;
			for (Match mp : temp) {
				
				if (m.getMatchId().equals(mp.getMatchId())) {
					isOk = false;
				}
			}
			if (isOk) {
				temp.add(m);
			}

		}

		return temp;
	}
	
	public static List<Match> getEntity(Element doc, String data)
			 {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHH:mm");
		SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<Match> matchs = new ArrayList<Match>();
		Elements links = doc.getElementsByTag("tr");
		Match m = null;
		for (Element link : links) {
			if (link.equals(links.get(0)))
				continue;
			Elements chiled = link.getElementsByTag("td");
			if (chiled.size() < 3 && m != null) {
				m.setNote(link.text());
				if (m.getMatchId()!=null) {
					matchs.add(m);
				}
				continue;
			}
			if (m != null&&m.getMatchId()!=null) {
				matchs.add(m);
			}

			m = new Match();
			m.setMatchName(chiled.get(0).text());
			try {
				m.setMatchTime(sd.parse(data
						+ chiled.get(1)
								.text()
								.substring(chiled.get(1).text().indexOf("日") + 1,
										chiled.get(1).text().length())));
			} catch (ParseException e1) {
				m.setMatchId(null);
				e1.printStackTrace();
			}
			m.setState(chiled.get(2).text());
			if("推迟".equals(m.getState())) continue;
			m.setHomeTeam(chiled.get(3).text());
			m.setResult(chiled.get(4).text());
			String str = chiled.get(4).attr("onclick");
			if(str==null||"".equals(str.trim())){
			  Elements element = chiled.get(9).getElementsByTag("a");
			  for(Element e: element){
				  str = e.attr("onclick");
				  if(str!=null&&str.trim().length()>5)continue;
			  }
			}
			if(str==null||"".equals(str.trim())) continue;
			str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
			m.setMatchId(Integer.parseInt(str));
			m.setGuestTeam(chiled.get(5).text());
			m.setCentre(chiled.get(6).text());
			m.setDish(chiled.get(7).text().trim());
			m.setSizeDish(chiled.get(8).text().trim());
			//System.out.println(m.toString());
			
			try {
				m.setHomeScpre(Integer.parseInt(m.getResult().split("\\-")[0]
						.trim()));
				m.setGuestScpre(Integer.parseInt(m.getResult().split("\\-")[1]
						.trim()));
				
				m.setHomeCentreScpre(Integer.parseInt(m.getCentre().split("\\-")[0]
						.trim()));
				
				m.setGuestCentreScpre(Integer
						.parseInt(m.getCentre().split("\\-")[1].trim()));
			} catch (Exception e) {
				m.setMatchId(null);
				e.printStackTrace();
			}
		}
		if (m != null&&m.getMatchId()!=null) {
			matchs.add(m);
		}
			
		return matchs;
	}

}
