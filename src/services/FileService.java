package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.DefFileModel;
import models.DefMasterModel;
import models.DefXmlInfoModel;
import rimwold_parsing.Main;

public class FileService {
	static String rootPath = "";
	static StringBuffer googleXmlText = new StringBuffer();
	static DefMasterModel defMasterModel = new DefMasterModel();

	/**
	 * 1번 옵션 파일 읽기 오리지널에서 파일 추출하는 파일 읽기
	 */
	public void openFile(String path) throws Exception {
		rootPath = path;
		File rootFolder = new File(rootPath);
		// 번역본을 만들 폴더
		File languagesFolder = null;
		String pathToCreateFolder = "";
		if (!rootFolder.isDirectory()) {
			throw new Exception("폴더가 존재 하지 않거나 권한이 부족합니다.");
		}
		System.out.println("모드 경로를 입력해주세요.");
		pathToCreateFolder = Main.inputSystemIn();
		languagesFolder = new File(pathToCreateFolder + "\\Languages\\korean");
		if (!languagesFolder.isDirectory()) {
			languagesFolder.mkdirs();
		}
		passingFile(rootFolder, languagesFolder);
		// google 번역용 파일 제작 로직
		createPassingGoogleXml(languagesFolder, languagesFolder);
		createGoogleXml(languagesFolder);

	};

	/**
	 * 
	 * @param folder          읽기 폴더
	 * @param languagesFolder 한국어 번역 폴더
	 * @throws IOException
	 */
	private void passingFile(File folder, File languagesFolder) throws IOException {
		File[] files = folder.listFiles();
		Pattern pattern = null;
		Matcher matcher = null;
		StringBuffer readText = new StringBuffer();
		String ParssingText = "";
		String line = "";
		String fullText = "";
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		FileWriter fileWriter = null;
		DefFileModel defXmlFileModel = null;
		DefXmlInfoModel defXmlInfoModel = null;
		String passingText = "";
		String createFolderPath = "";
		String[] pathArray;
		for (File file : files) {
			if (file.isDirectory()) {
				passingFile(file, languagesFolder);
			} else {
				// xml처리
				fileReader = new FileReader(file);
				bufferedReader = new BufferedReader(fileReader);
				// file read
				while ((line = bufferedReader.readLine()) != null) {
					readText.append(line + "\n");
				}
				bufferedReader.close();
				fileReader.close();
				// file read end

				fullText = readText.toString();
				// 원본에서 추출
				if (readText.indexOf("<Defs>") != -1) {
					pattern = Pattern.compile("<Defs>[\\s\\S]*?<\\/Defs");
					matcher = pattern.matcher(fullText);
					defXmlFileModel = new DefFileModel();
					defMasterModel.getDefPathList().add(defXmlFileModel);
					if (matcher.find()) {
						ParssingText = matcher.group(1);
					}
					// 정규식 검사로 다음 태그가 없지 않는한 계속 검사
					while (true) {
						pattern = Pattern.compile("<(.*?)>");
						matcher = pattern.matcher(passingText);
						if (matcher.find()) {
							// 마지막이 / 로 끝나거나 시작이 / 로 시작하면 제외
							String tag = matcher.group(1);
							String defName = "";
							String lable = "";
							String description = "";
							String path = "";
							if (tag.indexOf("/") != 1 && !tag.endsWith("/")) {
								// 옵션이 붙어 있을경우 옵션 제거
								path = tag.split(" ")[0];
								//파일 리스트에 해당 패치에 대한 키값이 없으면 생성
								//태그에 명시된 폴더 내용까지 파싱
								if (!defXmlFileModel.getPathMap().containsKey(path)) {
									defXmlFileModel.getPathMap().put(path, new ArrayList<DefXmlInfoModel>());
								}

							}

						} else {
							// 더 어싱 검사 할 태그가 없다면
							break;
						}

					}

				}

				// 다른 언어 번역 일경우
				if (fullText.indexOf("<LanguageData>") != -1) {
					fullText = fullText.replaceAll("<!--[\\s\\S]*?-->", "");
					int StartIndex = fullText.indexOf("<LanguageData>");
					int endIndex = fullText.indexOf("</LanguageData>");
					ParssingText += fullText.substring(StartIndex + "<LanguageData>".length(), endIndex);
					ParssingText += "</LanguageData>";
				}

//				// file write
//				createFolder = new File(languagesFolder + "\\" + createFolderPath);
//				if (!createFolder.isDirectory()) {
//					createFolder.mkdirs();
//				}
//				// 실제 xml 파일 제작 (pathArray 마지막 영역에는 반드시 파일이 들어있다)
//				createFile = new File(createFolder.getPath() + "\\" + pathArray[pathArray.length - 1]);
//				fileWriter = new FileWriter(createFile);
//				fileWriter.append(passingText);
//				fileWriter.flush();
//				fileWriter.close();
//				// 버퍼 초기화
//				readText.setLength(0);
//				passingText = "";
			}
		}
	}

	/**
	 * 파일 만들기전 하나의 스트링에 파일 내용 전부 읽기
	 * 
	 * @param languagesFolder 번역 폴더
	 * @param selectFile      선택된 폴더
	 * @throws IOException
	 */
	private void createPassingGoogleXml(File languagesFolder, File selectFile) throws IOException {
		File[] files = null;
		files = selectFile.listFiles();
		String relativePath = "";
		String line = "";
		BufferedReader bufferedReader = null;
		for (File file : files) {
			if (file.isDirectory()) {
				createPassingGoogleXml(languagesFolder, file);
			} else {
				// 앞에 경로 삭제(상대경로 취득)
				relativePath = selectFile.getPath().replace(languagesFolder.getPath(), "");
				if (relativePath.indexOf("\\") == 0) {
					relativePath = relativePath.substring(1);
				}
				relativePath = "[korean.passing::" + relativePath + "\\" + file.getName().replace(".xml", "") + "]\n";
				googleXmlText.append(relativePath);
				bufferedReader = new BufferedReader(new FileReader(file));
				while ((line = bufferedReader.readLine()) != null) {
					googleXmlText.append(line + "\n");
				}
				googleXmlText.append(relativePath.replace("[korean.passing::", "[/korean.passing::"));
				relativePath = "";
			}
		}
	}

	private void createGoogleXml(File languagesFolder) throws IOException {
		BufferedWriter bufferedWriter = new BufferedWriter(
				new FileWriter(new File(languagesFolder.getPath() + "\\google_original.xml")));
		bufferedWriter.write(googleXmlText.toString());
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	/**
	 * 옵션 2번용 파일 함수
	 * 
	 * @throws IOException
	 */

	public void readGoogleFileXml(String xmlFilePath) throws IOException {
		String pathToCreateFolder = "";
		// 줄 읽기를 위한 변수
		String line = "";
		// 파일 전체 텍스트
		String fileText = "";
		String parssingText = "";
		String fullPath = "";
		String folderPath = "";
		String filePath = "";
		// 읽은 파일을 임시로 받을 버퍼
		StringBuffer readText = new StringBuffer();
		File xmlFile = new File(xmlFilePath);
		// xml에서 분리된 정보를 파일로 바꾸기 위한 변수

		File tmpFile = null;
		File languagesFolder = null;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(xmlFile));
		BufferedWriter bufferedWriter = null;
		Pattern pattern = Pattern.compile("\\[\\s*korean\\.passing::([^\\]]+)\\]", Pattern.DOTALL);
		Matcher matcher = null;
		System.out.println("모드 경로를 입력해주세요. 마지막이 번호 폴더로 끝나야 합니다.");
		pathToCreateFolder = Main.inputSystemIn();
		languagesFolder = new File(pathToCreateFolder + "\\Languages\\korean");
		while ((line = bufferedReader.readLine()) != null) {
			readText.append(line + "\n");
		}
		fileText = readText.toString();
		// 읽기까지 종료

		while (true) {
			parssingText = fileText.substring(fileText.indexOf("[korean.passing::"),
					(fileText.indexOf("[/korean.passing::")));
			matcher = pattern.matcher(parssingText);
			while (matcher.find()) {
				fullPath = matcher.group(1); // 괄호 그룹의 첫 번째
			}
			parssingText = parssingText.replaceAll("\\[/?korean\\.passing::[^\\]]+\\]\n", "");
			// 아직은 xml만 있기 떄문에 xml로 강제 코딩
			String[] tmp = fullPath.split("\\\\");
			for (int c = 0; c < tmp.length; c++) {
				// 파일명 처리
				if (c == tmp.length - 1) {
					filePath = tmp[c] + ".xml";
				} else {
					folderPath += tmp[c] + "\\";
				}
			}
			tmpFile = new File(languagesFolder + "\\" + folderPath);
			// 폴더가 없는경우 생성
			if (!tmpFile.isDirectory()) {
				tmpFile.mkdirs();
			}
			tmpFile = null;
			// 우선 태그를 닫는데까지 진행
			fileText = fileText.substring(fileText.indexOf("[/korean.passing::"));
			tmpFile = new File(languagesFolder + "\\" + folderPath + filePath);
			bufferedWriter = new BufferedWriter(new FileWriter(tmpFile));
			// 2025-06-13 .description> 이후에는 공백은 존재하면 안됨
			parssingText = parssingText.replaceAll("(<[^>]+>)\\s+", "$1");
			parssingText = parssingText.replaceAll("\\s+(</[^>]+>)", "$1");
			bufferedWriter.write(parssingText);
			bufferedWriter.flush();
			bufferedWriter.close();
			// 다음 파일이 여부 검수 없으면 종료
			fullPath = "";
			folderPath = "";
			filePath = "";
			if (fileText.indexOf("[korean.passing::") == -1) {
				break;
			}
			// 다음 태그까지 이동
			fileText = fileText.substring(fileText.indexOf("[korean.passing::"));
		}
		bufferedReader.close();
		System.out.println("변환이 종료 되었습니다.");
	}

}
