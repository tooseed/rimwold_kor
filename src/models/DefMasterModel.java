package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefMasterModel {
	private List<DefFileModel> defFileist;

	public DefMasterModel() {
		this.defFileist = new ArrayList<DefFileModel>();
	}

	public List<DefFileModel> getDefPathList() {
		return defFileist;
	}

}
