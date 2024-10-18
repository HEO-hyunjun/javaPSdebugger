package javaPSdebugger.util;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class DebugConfigure {
	public boolean PRINT = true;
	public boolean USE_INPUT_FILE = false;
	public String INPUT_FILE = "input.txt";

	public boolean PRINT_WITH_HR = true;
	public boolean IGNORE_MIN_MAX_VAL = true;
	public char IGNORE_CHAR = '&';
	public char DEFAULT_HR_CHAR = '*';
	public int DEFAULT_HR_CNT = 45;
	public boolean CREATE_SUBMIT_CODE = false;
	public boolean CREATE_JAVA_FILE = false;
	public boolean PRINT_WITH_INDEX = true;
	public String SUBMIT_FILE_NAME = "submit.txt";
	public String SUBMIT_CLASS_NAME = "Main";

	static final String ROOT_PATH = System.getProperty("user.dir");
	String DEBUG_PACKAGE_PATH;
	String DEBUG_CLASS;
	String DEBUG_JAVA_FILE;

	public static enum InfoDetail {
		NONE, SIMPLE, DETAIL
	}

	public InfoDetail SHOW_INFO_DETAIL = InfoDetail.SIMPLE;

	public DebugConfigure() {
		if (DEBUG_PACKAGE_PATH == null)
			DEBUG_PACKAGE_PATH = findFilePathFrom(ROOT_PATH, "Debug.java");

		loadINI();
	}

	public DebugConfigure(DebugConfigure customConfig, Class nowClass) {
		this.PRINT = customConfig.PRINT;
		this.USE_INPUT_FILE = customConfig.USE_INPUT_FILE;
		this.PRINT_WITH_HR = customConfig.PRINT_WITH_HR;
		this.IGNORE_MIN_MAX_VAL = customConfig.IGNORE_MIN_MAX_VAL;
		this.IGNORE_CHAR = customConfig.IGNORE_CHAR;
		this.DEFAULT_HR_CHAR = customConfig.DEFAULT_HR_CHAR;
		this.DEFAULT_HR_CNT = customConfig.DEFAULT_HR_CNT;
		this.CREATE_SUBMIT_CODE = customConfig.CREATE_SUBMIT_CODE;
		this.CREATE_JAVA_FILE = customConfig.CREATE_JAVA_FILE;
		this.PRINT_WITH_INDEX = customConfig.PRINT_WITH_INDEX;
		this.SUBMIT_FILE_NAME = customConfig.SUBMIT_FILE_NAME;
		this.SUBMIT_CLASS_NAME = customConfig.SUBMIT_CLASS_NAME;
		this.INPUT_FILE = customConfig.INPUT_FILE;
		init(nowClass);
		writeSubmitCode();
		setUseInputFile();
	}

	public DebugConfigure(Object nowClass) {
		init(nowClass.getClass());
		loadINI();
		writeSubmitCode();
		setUseInputFile();
	}

	public DebugConfigure(Class nowClass) {
		init(nowClass);
		loadINI();
		writeSubmitCode();
		setUseInputFile();
	}

	private void init(Class nowClass) {
		if (DEBUG_PACKAGE_PATH == null)
			DEBUG_PACKAGE_PATH = findFilePathFrom(ROOT_PATH, "Debug.java");

		String[] classFullName = nowClass.getName().split("\\.");
		if (classFullName.length > 1)
			DEBUG_CLASS = classFullName[classFullName.length - 1];
		else
			DEBUG_CLASS = nowClass.getTypeName();

		DEBUG_JAVA_FILE = findFileFrom(ROOT_PATH, DEBUG_CLASS + ".java");

		String[] submitFilePath = SUBMIT_FILE_NAME.split("/");
		if (submitFilePath.length > 1) {
			StringBuilder path = new StringBuilder();
			path.append(findFilePathFrom(ROOT_PATH, submitFilePath[submitFilePath.length - 2]));
			path.append(File.separator).append(submitFilePath[submitFilePath.length - 1]);
			SUBMIT_FILE_NAME = path.toString();
		} else if (submitFilePath.length == 1) {
			SUBMIT_FILE_NAME = submitFilePath[submitFilePath.length - 1];
		}
	}

	private void writeSubmitCode() {
		if (CREATE_SUBMIT_CODE) {
			try {
				File debugFile = new File(DEBUG_JAVA_FILE);
				File submitFile = new File(SUBMIT_FILE_NAME);
				BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(submitFile), "utf-8"));
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(debugFile), "utf-8"));
				String s;
				Pattern methodRegex = Pattern.compile(
						"(?i)(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)\\s+)*(?:\\w+\\s+)*\\w*(?:debug)\\w*\\s+\\w+\\s*\\([^)]*\\)");
				boolean isCoordinateDebugger = false;
				boolean removeGetRowGetCol = false;

				while ((s = br.readLine()) != null) {
					// 1. 클래스 이름 수정
					if (s.contains(DEBUG_CLASS) && s.contains("class")) {
						s = s.replace(DEBUG_CLASS, SUBMIT_CLASS_NAME);
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

					// 2-1, 2-2. getCol(), getRow()의 @Override 삭제
					if (removeGetRowGetCol) {
						if (s.trim().equals("@Override")) {
							String nextLine = br.readLine();
							if (nextLine != null && (nextLine.contains("getRow()") || nextLine.contains("getCol()"))) {
								s = nextLine; // @Override 애노테이션 삭제
							} else {
								bw.write(s + '\n'); // 다른 메서드의 @Override는 유지
								s = nextLine; // 다음 줄 처리를 위해 s 업데이트
							}
						}
						if (s.contains("getRow()") || s.contains("getCol()")) {
							int openBracesCount = 1;
							while (openBracesCount > 0 && (s = br.readLine()) != null) {
								openBracesCount += countChar(s, '{');
								openBracesCount -= countChar(s, '}');
							}
							continue; // 메서드 블록을 건너뜀
						}
					}

					// "Debug" 관련 메서드 삭제: 메서드 선언부인지 확인
					if (methodRegex.matcher(s).find()) {
						int openBracesCount = 1;
						while (openBracesCount > 0 && (s = br.readLine()) != null) {
							openBracesCount += countChar(s, '{');
							openBracesCount -= countChar(s, '}');
						}
						continue; // Debug 메서드 전체를 건너뜀
					}

					// 3. Debug 관련 라인 삭제
					if (s.toUpperCase().contains("DEBUG")) {
						while (!s.contains(";") && (s = br.readLine()) != null)
							;
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

	private int countChar(String line, char character) {
		int count = 0;
		for (char ch : line.toCharArray()) {
			if (ch == character)
				count++;
		}
		return count;
	}

	private void setUseInputFile() {
		if (USE_INPUT_FILE) {
			try {
				INPUT_FILE = findFileFrom(ROOT_PATH, INPUT_FILE);
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
			p.load(new InputStreamReader(new FileInputStream(configFile), "utf-8"));

			USE_INPUT_FILE = getProperty(p, "USE_INPUT_FILE", USE_INPUT_FILE);
			INPUT_FILE = getProperty(p, "INPUT_FILE", INPUT_FILE);

			PRINT = getProperty(p, "PRINT", PRINT);
			PRINT_WITH_HR = getBoolean(p.getProperty("PRINT_WITH_HR"));
			IGNORE_MIN_MAX_VAL = getProperty(p, "IGNORE_MIN_MAX_VAL", IGNORE_MIN_MAX_VAL);
			IGNORE_CHAR = getProperty(p, "IGNORE_CHAR", IGNORE_CHAR);

			DEFAULT_HR_CHAR = getProperty(p, "DEFAULT_HR_CHAR", DEFAULT_HR_CHAR);
			DEFAULT_HR_CNT = getProperty(p, "DEFAULT_HR_CNT", DEFAULT_HR_CNT);

			PRINT_WITH_INDEX = getProperty(p, "PRINT_WITH_INDEX", PRINT_WITH_INDEX);

			CREATE_SUBMIT_CODE = getProperty(p, "CREATE_SUBMIT_CODE", CREATE_SUBMIT_CODE);
			CREATE_JAVA_FILE = getProperty(p, "CREATE_JAVA_FILE", CREATE_JAVA_FILE);

			SUBMIT_CLASS_NAME = getProperty(p, "SUBMIT_CLASS_NAME", SUBMIT_CLASS_NAME);
			SUBMIT_FILE_NAME = getProperty(p, "SUBMIT_FILE_NAME", SUBMIT_FILE_NAME);

			if (CREATE_JAVA_FILE)
				SUBMIT_FILE_NAME = SUBMIT_CLASS_NAME + ".java";

			SUBMIT_FILE_NAME = ROOT_PATH + File.separator + SUBMIT_FILE_NAME;

			String infoDetail = getProperty(p, "SHOW_INFO_DETAIL", "SIMPLE");
			SHOW_INFO_DETAIL = getInfoDetail(infoDetail);

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
				String tmp = findFilePathFrom(file.getPath(), target);
				ret = tmp == "" ? ret : tmp;
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
				String tmp = findFileFrom(file.getPath(), target);
				ret = tmp == "" ? ret : tmp;
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

	private InfoDetail getInfoDetail(String detail) {
		if (detail.equalsIgnoreCase("detail")) {
			return InfoDetail.DETAIL;
		} else if (detail.equalsIgnoreCase("simple")) {
			return InfoDetail.SIMPLE;
		} else {
			return InfoDetail.NONE;
		}
	}

	private boolean getBoolean(String s) {
		return s.compareToIgnoreCase("true") == 0;
	}
}
