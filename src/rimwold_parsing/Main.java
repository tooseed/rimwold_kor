package rimwold_parsing;

import java.util.Scanner;

import enums.OptionEnum;
import services.FileService;
import services.OptionService;
import services.PathService;

public class Main {
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		OptionService optionService = new OptionService();
		PathService pathService = new PathService();
		FileService fIleService = new FileService();
		OptionEnum optionEnum = optionService.getEnumOption();
		switch (optionEnum) {
		case ORIGINAL_PASSING_OPTION:
			String folderPath = pathService.scannerOriginalPath();
			fIleService.openFile(folderPath);
			System.out.println("모드 파일 Languages -> korean에 생성 되었습니다.");
			break;
		case KOR_GOOGLE_TO_FILE_OPTION:
			System.out.println("번역된 파일의 경로을 포함한 파일명은 입력 해주세요");
			String filePath = inputSystemIn();
			fIleService.readGoogleFileXml(filePath);
			break;
		}
	}

	public static String inputSystemIn() {
		return scanner.nextLine();

	}
}
