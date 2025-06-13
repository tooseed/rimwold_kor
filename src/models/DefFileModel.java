package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 파일 이름과 파일 리스트를 저장합니다. 단 우DefXmlPath에서 패치를 먼저 확인하자
 */

public class DefFileModel {
	private String name;
	// <폴더위치,파일정보> 같은 폴더 끼리 합치기
	private Map<String, List<DefXmlInfoModel>> pathMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, List<DefXmlInfoModel>> getPathMap() {
		return pathMap;
	}

	public DefFileModel() {
		this.pathMap = new HashMap<String, List<DefXmlInfoModel>>();
		this.name = "";
	}

}
