package com.smhrd.service;

import java.util.List;

public interface StylePrefService {


	void saveStylePref(String id, List<String> styleCodes);


	void updateStylePref(String id, List<String> styleCodes);

	
	
}
