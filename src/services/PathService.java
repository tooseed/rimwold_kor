package services;

import java.io.File;
import java.util.Scanner;

import rimwold_parsing.Main;

public class PathService {
	public String scannerOriginalPath() {
		System.out.println("탐색할 def 폴더 경로를 입력해주세요");
		String folderPath = null;
		File originaFolder = null;
		boolean isFoler = false;
		while (true) {
			folderPath = Main.inputSystemIn();
			originaFolder = new File(folderPath);
			isFoler = originaFolder.isDirectory();
			//폴더 존재시 탈출
			if(isFoler) {
				break;
			}
			System.out.println("존재 하지 않는 폴더 입니다. 다시 입력 해주세요");
		}
		return folderPath;
	}
	
}
