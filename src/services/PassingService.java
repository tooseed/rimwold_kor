package services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassingService {
	public String parssing(String text) {
		/**
		 * 내용을 추출을 위한 패턴
		 */
		Pattern defNamePattern = Pattern.compile("<defName>(.*?)</defName>", Pattern.DOTALL);
		Pattern lableNamePattern = Pattern.compile("<label>(.*?)</label>",Pattern.DOTALL);
		Pattern descriptionPattern = Pattern.compile("<description>(.*?)</description>",Pattern.DOTALL);
		Matcher matcher = null;
		String nextText = text;
		// 번역을 위한 기본 내용
		String ParssingText = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n" + "<LanguageData>\n";
		/**
		 * 추출 변수
		 */
		// def 고유 아이디 추출 내용
		String defName = "";
		// 레이블 추출 변수
		String lableName = "";
		// 설명 추출 변수
		String descripton = "";
		int defnameIndex = 0, lableIndex, descriptionIndex;
		int endDefnameIndex, endLableIndex, endDescriptionIndex;
		while (true) {
			// def name areaD:\SteamLibrary\steamapps\workshop\content\294100\3472275628\1.5\Defs
			defnameIndex = getNextPointDefName(nextText);
			if (defnameIndex == -1) {
				break;
			}
			endDefnameIndex = getNextEndPointDefName(nextText);
			matcher = defNamePattern.matcher(nextText.substring(defnameIndex, endDefnameIndex + "</defName>".length()));
			while (matcher.find()) {
				defName = matcher.group(1); // 괄호 그룹의 첫 번째
			}

			nextText = nextText.substring(endDefnameIndex + "</defName>".length());
			// lable area
			lableIndex = getNextPointLable(nextText);
			endLableIndex = getNextEndPointLable(nextText);
			if (lableIndex != -1) {
				matcher = lableNamePattern.matcher(nextText.substring(lableIndex, endLableIndex + "</label>".length()));
				while (matcher.find()) {
					lableName = matcher.group(1); // 괄호 그룹의 첫 번째
				}
				/**
				 * 원래는 정규식으로 처리 할려고 했으나 문제가 자꾸 발생함 향후 제대로 짜서 변경
				 */
				ParssingText += "\t<" + defName + ".label>";
				ParssingText += lableName;
				ParssingText += "</" + defName + ".label>\n";
				nextText = nextText.substring(endLableIndex + "</label>".length());
			}
			// description area
			descriptionIndex = getNextPointDescription(nextText);
			endDescriptionIndex = getNextEndPointDescription(nextText);
			if (descriptionIndex != -1) {
				matcher = descriptionPattern
						.matcher(nextText.substring(descriptionIndex, endDescriptionIndex + "</description>".length()));
				while (matcher.find()) {
					descripton = matcher.group(1); // 괄호 그룹의 첫 번째
				}

				ParssingText += "\t" +"<" + defName + ".description>";
				ParssingText += descripton ;
				ParssingText +=  "</" + defName + ".description>\n";
				nextText = nextText.substring(endDescriptionIndex + "</description>".length());
			}

		}
		ParssingText += "</LanguageData>";
		return ParssingText;
	}

	private int getNextPointDefName(String text) {
		return text.indexOf("<defName>");
	};

	private int getNextPointLable(String text) {
		return text.indexOf("<label>");
	};

	private int getNextPointDescription(String text) {
		return text.indexOf("<description>");
	};

	private int getNextEndPointDefName(String text) {
		return text.indexOf("</defName>");
	};

	private int getNextEndPointLable(String text) {
		return text.indexOf("</label>");
	};

	private int getNextEndPointDescription(String text) {
		return text.indexOf("</description>");
	};

}
