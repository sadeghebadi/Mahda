package ir.rayacell.mahdaclient.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DateTimeManager {

	public HashMap<String, Integer> parseDateTime(String datetime) {
		ArrayList<Integer> line_index = new ArrayList<Integer>();
		Map<String, Integer> m_d_t = new HashMap<String, Integer>();
		for (int i = 0; i < datetime.length(); i++) {
			if (datetime.charAt(i) == '-') {
				line_index.add(i);
			}
		}

		m_d_t.put("year",
				Integer.parseInt(datetime.substring(0, line_index.get(0))));
		m_d_t.put("month", Integer.parseInt(datetime.substring(
				line_index.get(0) + 1, line_index.get(1))));
		m_d_t.put("day", Integer.parseInt(datetime.substring(
				line_index.get(1) + 1, line_index.get(2))));
		m_d_t.put("hour", Integer.parseInt(datetime.substring(
				line_index.get(2) + 1, line_index.get(3))));
		m_d_t.put("minute",
				Integer.parseInt(datetime.substring(line_index.get(3) + 1)));

		return (HashMap<String, Integer>) m_d_t;
	}
}
