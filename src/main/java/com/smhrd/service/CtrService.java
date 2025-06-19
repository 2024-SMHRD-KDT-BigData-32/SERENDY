package com.smhrd.service;

import java.util.List;
import java.util.Map;

public interface CtrService {

	Map<String, String> getMostFrequentFitAndColor(String userId);

	List<String> getStylePref(String userId);

	String getAge(String userId);

}
