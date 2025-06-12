package services;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import enums.OptionEnum;
import rimwold_parsing.Main;

public class OptionService {
	public OptionEnum getEnumOption() {
		OptionEnum enumOptionEnum = null;
		System.out.println("옵션을 선택 해주세요");
		System.out.println("1.언어 관련을 추출합니다.");
		System.out.println("2.통합 번역 파일을 다시 풉니다.");
		String optionNumber = "";

		while (enumOptionEnum == null) {
			optionNumber = Main.inputSystemIn();
			switch (optionNumber) {
			case "1": {
				enumOptionEnum = OptionEnum.ORIGINAL_PASSING_OPTION;
				break;
			}
			case "2": {
				enumOptionEnum = OptionEnum.KOR_GOOGLE_TO_FILE_OPTION;
				break;
			}
			default:
				System.out.println("잘못 입력 하였습니다.");
				System.out.println("다시 입력 해주세요");
			}
		}
		return enumOptionEnum;
	}

}
