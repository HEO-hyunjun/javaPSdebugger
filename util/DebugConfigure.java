package javaPSdebugger.util;

import java.io.*;
import java.util.*;

public class DebugConfigure {
	public boolean USE_INPUT_FILE = false;
	public boolean PRINT = true;

	public boolean PRINT_WITH_HR = true;
	public boolean IGNORE_MIN_MAX_VAL = true;
	public char IGNORE_CHAR = '&';
	public char DEFAULT_HR_CHAR = '*';
	public int DEFAULT_HR_CNT = 45;
	public boolean AUTO_WRITE_SUBMIT_CODE = false;
	public boolean PRINT_WITH_INDEX = true;
	public String AUTO_SUBMIT_FILE_NAME = "submit.txt";
	public String AUTO_SUBMIT_CLASS_NAME = "Main";

	final String ROOT_PATH = System.getProperty("user.dir");
	String DEBUG_PACKAGE_PATH;
	String INPUT_FILE = "input.txt";
	String DEBUG_CLASS;
	String DEBUG_JAVA_FILE;

	public DebugConfigure(Object nowClass) {
		init(nowClass);
	}

	private void init(Object nowClass) {
		DEBUG_PACKAGE_PATH = findFilePathFrom(ROOT_PATH, "Debug.java");
		DEBUG_CLASS = nowClass.getClass().getName();
		loadINI();
		writeSubmitCode();
		setUseInputFile();
	}

	private void writeSubmitCode() {
		if (AUTO_WRITE_SUBMIT_CODE) {
			try {
				File debugFile = new File(DEBUG_JAVA_FILE);
				File submitCode = new File(AUTO_SUBMIT_FILE_NAME);
				BufferedWriter bw = new BufferedWriter(new FileWriter(submitCode));
				BufferedReader br = new BufferedReader(new FileReader(debugFile));
				String s;
				boolean isCoordinateDebugger = false;
				boolean removeGetRowGetCol = false;

				while ((s = br.readLine()) != null) {
					// 1. 클래스 이름 수정
					if (s.contains(DEBUG_CLASS) && s.contains("class")) {
						s = s.replace(DEBUG_CLASS, AUTO_SUBMIT_CLASS_NAME);
					}

					// 2. 인터페이스 상속 확인 및 처리
					if (s.contains("implements")) {
						String originalLine = s;
						s = s.substring(0, s.indexOf("implements")).trim();
						String interfaces = originalLine
								.substring(originalLine.indexOf("implements") + "implements".length()).trim();

						// 인터페이스들 나누기
						String[] implementedInterfaces = interfaces.replace("{", "").split(",");

						List<String> remainingInterfaces = new ArrayList<>();
						isCoordinateDebugger = false;
						for (String iface : implementedInterfaces) {
							iface = iface.trim();
							if (iface.equals("CoordinateDebugger")) {
								isCoordinateDebugger = true;
							} else {
								remainingInterfaces.add(iface);
							}
						}

						// CoordinateDebugger만 상속한 경우
						if (isCoordinateDebugger && remainingInterfaces.isEmpty()) {
							removeGetRowGetCol = true;
						} else {
							// 다른 인터페이스들이 남아있는 경우, CoordinateDebugger만 삭제
							if (!remainingInterfaces.isEmpty()) {
								s += " implements " + String.join(", ", remainingInterfaces);
								removeGetRowGetCol = true;
							}
						}
						s += " {";
					}

					// 2-1, 2-2. getCol(), getRow(), @Override 삭제
					if (removeGetRowGetCol) {
						if (s.contains("@Override")) {
							continue; // @Override 애노테이션 삭제
						}
						if (s.contains("getRow()") || s.contains("getCol()")) {
							while (!(s = br.readLine()).contains("}")) {
								// 메서드 내부 내용 스킵
							}
							continue; // 메서드 블록을 건너뜀
						}
					}

					// 3. Debug 관련 라인 삭제
					if (s.toUpperCase().contains("DEBUG")) {
						continue; // Debug가 포함된 라인은 삭제
					}

					// 파일에 최종 라인 작성
					bw.write(s + '\n');
				}

				bw.flush();
				bw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setUseInputFile() {
		if (USE_INPUT_FILE) {
			try {
				System.setIn(new FileInputStream(new File(INPUT_FILE)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadINI() {
		try {
			Properties p = new Properties();
			String configFile = DEBUG_PACKAGE_PATH + File.separator + "Config.ini";
			p.load(new FileInputStream(configFile));

			USE_INPUT_FILE = getProperty(p, "USE_INPUT_FILE", USE_INPUT_FILE);
			INPUT_FILE = getProperty(p, "INPUT_FILE", INPUT_FILE);
			INPUT_FILE = findFileFrom(ROOT_PATH, INPUT_FILE);

			PRINT = getProperty(p, "PRINT", PRINT);
			PRINT_WITH_HR = getBoolean(p.getProperty("PRINT_WITH_HR"));
			IGNORE_MIN_MAX_VAL = getProperty(p, "IGNORE_MIN_MAX_VAL", IGNORE_MIN_MAX_VAL);
			IGNORE_CHAR = getProperty(p, "IGNORE_CHAR", IGNORE_CHAR);

			DEFAULT_HR_CHAR = getProperty(p, "DEFAULT_HR_CHAR", DEFAULT_HR_CHAR);
			DEFAULT_HR_CNT = getProperty(p, "DEFAULT_HR_CNT", DEFAULT_HR_CNT);

			PRINT_WITH_INDEX = getProperty(p, "PRINT_WITH_INDEX", PRINT_WITH_INDEX);

			AUTO_WRITE_SUBMIT_CODE = getProperty(p, "AUTO_WRITE_SUBMIT_CODE", AUTO_WRITE_SUBMIT_CODE);
			AUTO_SUBMIT_FILE_NAME = getProperty(p, "AUTO_SUBMIT_FILE_NAME", AUTO_SUBMIT_FILE_NAME);
			AUTO_SUBMIT_FILE_NAME = ROOT_PATH + File.separator + AUTO_SUBMIT_FILE_NAME;
			AUTO_SUBMIT_CLASS_NAME = getProperty(p, "AUTO_SUBMIT_CLASS_NAME", AUTO_SUBMIT_CLASS_NAME);

			DEBUG_JAVA_FILE = findFileFrom(ROOT_PATH, DEBUG_CLASS + ".java");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String findFilePathFrom(String dirPath, String target) {
		File dir = new File(dirPath);
		File files[] = dir.listFiles();
		String ret = "";
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				ret = findFilePathFrom(file.getPath(), target);
			} else {
				if (file.toString().contains(target)) {
					return dir.getPath();
				}
			}
		}
		return ret;
	}

	private String findFileFrom(String dirPath, String target) {
		File dir = new File(dirPath);
		File files[] = dir.listFiles();
		String ret = "";
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				ret = findFileFrom(file.getPath(), target);
			} else {
				if (file.toString().contains(target)) {
					return file.toString();
				}
			}
		}
		return ret;
	}

	private boolean getProperty(Properties p, String find, boolean property) {
		return p.getProperty(find) == null ? property : getBoolean(p.getProperty(find).trim());
	}

	private char getProperty(Properties p, String find, char property) {
		return p.getProperty(find) == null ? property : p.getProperty(find).trim().charAt(0);
	}

	private String getProperty(Properties p, String find, String property) {
		return p.getProperty(find) == null ? property : p.getProperty(find).trim();
	}

	private int getProperty(Properties p, String find, int property) {
		return p.getProperty(find) == null ? property : Integer.parseInt(p.getProperty(find).trim());
	}

	private boolean getBoolean(String s) {
		return s.compareToIgnoreCase("true") == 0;
	}
}
