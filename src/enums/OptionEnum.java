package enums;

public enum OptionEnum {
	ORIGINAL_PASSING_OPTION(1), KOR_GOOGLE_TO_FILE_OPTION(2);

	private final int value;

	OptionEnum(int i) {
		this.value = i;
	}
	public int getValue() {
		return this.value;
	}
	
}
