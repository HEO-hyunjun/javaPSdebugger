package javaPSdebugger;

import java.io.*;

import javaPSdebugger.util.CoordinateDebugger;
import javaPSdebugger.util.DebugConfigure;
import javaPSdebugger.util.DebugPrint;

class CoordinateImpl implements CoordinateDebugger {
	int y, x;

	public CoordinateImpl(int y, int x) {
		this.y = y;
		this.x = x;
	}

	@Override
	public int getRow() {
		return y;
	}

	@Override
	public int getCol() {
		return x;
	}
}

/**
 * This file was written in UTF-8 format. So, If you can't read Korean letters,
 * change Editor encoding setting to UTF-8
 * 
 * @author HEO-hyunjun
 * 
 */
public class Debug {
	private static Debug instance = new Debug();
	private boolean USE_INPUT_FILE = false;
	private final String ROOT_PATH = System.getProperty("user.dir");
	private String DEBUG_PACKAGE_PATH;
	private String inputFile = "input.txt";
	private boolean PRINT = false;
	private long timer;

	public static DebugConfigure config = new DebugConfigure();
	public static DebugPrint print = new DebugPrint(instance);
	

	private Debug() {
		System.out.println("Debug 모듈이 import됐습니다.");
		System.out.println("^.*(Debug).*");
		System.out.println("find and replace(ctrl+F)로 위 정규식을 입력후, 공백으로 replace하여 제출해주세요");

		init();
	}

	private void init() {
		System.out.println(findFilesFrom(ROOT_PATH, "Debug.java"));
	}

	private String findFilesFrom(String dirPath, String target) {
		File dir = new File(dirPath);
		File files[] = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				findFilesFrom(file.getPath(), target);
			} else {
				System.out.println(file.toString() + " " + file.toString().contains(target));
				if (file.toString().contains(target)) {
					return file.toString();
				}
			}
		}
		return null;
	}

	// **************************************************************************START/setting
	/**
	 * 디버그 모드를 설정합니다. 디버그 모드가 활성화되면 지정된 입력 파일을 사용합니다.
	 *
	 * @param debug 디버그 모드를 활성화하려면 true, 비활성화하려면 false
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setDebug(boolean print) throws IOException {
		printDebugStatus();
		instance.PRINT = print;
		setInputFile(instance.USE_INPUT_FILE);
	}

	/**
	 * 입력 파일 사용 여부를 설정합니다. 디버그 모드가 활성화된 상태에서 입력 파일을 사용하도록 설정하면, 시스템 입력 스트림을 지정된 파일로
	 * 대체합니다.
	 *
	 * @param set 입력 파일을 사용하려면 true, 사용하지 않으려면 false
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setInputFile(boolean set) throws IOException {
		instance.USE_INPUT_FILE = set;
		if (instance.USE_INPUT_FILE)
			System.setIn(new FileInputStream(new File(instance.inputFile)));
	}

	// **************************************************************************END/start

	// **************************************************************************START/printArr
	// *******************************************START/int[][]
	// *******************************************START/actualFunction
	/**
	 * int 2차원 배열을 출력합니다. 지정된 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr    출력할 배열
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", config.IGNORE_CHAR);
				else
					System.out.printf("%d ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 지정된 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", config.IGNORE_CHAR);
				else
					System.out.printf("%d ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}
				if (flag)
					System.out.printf("%c ", chkChar);
				else if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", config.IGNORE_CHAR);
				else
					System.out.printf("%d ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, CoordinateDebugger[] cors, char chkChar, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}
				if (flag)
					System.out.printf("%c ", chkChar);
				else if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", config.IGNORE_CHAR);
				else
					System.out.printf("%d ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	// *******************************************END/actualFunction

	/**
	 * int 2차원 배열을 출력합니다. 디버그 모드가 활성화된 경우에만 배열을 출력합니다.
	 *
	 * @param arr 출력할 배열
	 */
	public static void printArr(int[][] arr, String... startText) {
		if (!instance.PRINT)
			return;
		printArr(arr, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 디버그 모드가 활성화된 경우에만 배열을 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, row, col, chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int row, int col, char chkChar, int ignore,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, new CoordinateImpl(row, col), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int row, int col, char chkChar, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, new CoordinateImpl(row, col), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int row, int col, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, new CoordinateImpl(row, col), chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     출력을 대체할 문자
	 * @param ignore      무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int[][] coordinates, char chkChar, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, int[][] coordinates, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     출력을 대체할 문자
	 * @param ignore      무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int[][] coordinates, char chkChar, int ignore,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int[][] coordinates, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, convertIntArrToCoordinateArr(coordinates), chkChar, Integer.MAX_VALUE,
				startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger cor, char chkChar, int ignore,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, convertCoordinateToArr(cor), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger cor, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, convertCoordinateToArr(cor), chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, CoordinateDebugger cor, char chkChar, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertCoordinateToArr(cor), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, CoordinateDebugger cor, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertCoordinateToArr(cor), chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;
		printArr(arr, rowSize, colSize, cors, chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[][] arr, CoordinateDebugger[] cors, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;
		printArr(arr, cors, chkChar, Integer.MAX_VALUE, startText);
	}
	// *******************************************END/int[][]

	// *******************************************START/int[]
	/**
	 * int 1차원 배열을 출력합니다.
	 *
	 * @param arr 출력할 배열
	 */
	public static void printArr(int[] arr, String... startText) {
		if (!instance.PRINT)
			return;
		printArr(arr, 0, arr.length, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열을 출력합니다. 배열의 값이 ignore 값과 같다면, 0으로 출력합니다.
	 *
	 * @param arr    출력할 배열
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, ignore, startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다.
	 *
	 * @param arr   출력할 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 */
	public static void printArr(int[] arr, int start, int end, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			if (chkIgnore(arr[i], ignore))
				System.out.printf("%c ", config.IGNORE_CHAR);
			else
				System.out.printf("%d ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. ignore 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr    출력할 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int start, int end, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, start, end, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다. ignore 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int start, int end, int chkIdx, char chkChar, int ignore,
			String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			if (i == chkIdx)
				System.out.printf("%c ", chkChar);
			else if (chkIgnore(arr[i], ignore))
				System.out.printf("%c ", config.IGNORE_CHAR);
			else
				System.out.printf("%d ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[] arr, int start, int end, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, start, end, chkIdx, chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열의 전체 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다. ignore 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int chkIdx, char chkChar, int ignore, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, ignore, startText);
	}

	/**
	 * int 1차원 배열의 전체를 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[] arr, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, Integer.MAX_VALUE, startText);
	}

	// *******************************************END/int[]

	// *******************************************START/char
	/**
	 * char 2차원 배열의 일부분을 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				System.out.printf("%c ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다.
	 * 
	 * @param arr 출력할 배열
	 */
	public static void printArr(char[][] arr, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				System.out.printf("%c ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정 위치의 값들을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    특정 위치들이 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					if (i == cors[k].getRow() && j == cors[k].getCol()) {
						flag = true;
						break;
					}
				}

				if (flag)
					System.out.printf("%c ", chkChar);
				else
					System.out.printf("%c ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값들을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param cors    특정 위치들이 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, CoordinateDebugger[] cors, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					if (i == cors[k].getRow() && j == cors[k].getCol()) {
						flag = true;
						break;
					}
				}

				if (flag)
					System.out.printf("%c ", chkChar);
				else
					System.out.printf("%c ", arr[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param cors    특정 위치가 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, CoordinateDebugger cor, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertCoordinateToArr(cor), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    특정 위치가 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, CoordinateDebugger cor, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, convertCoordinateToArr(cor), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param cors    특정 위치들이 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int[][] cors, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, convertIntArrToCoordinateArr(cors), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    특정 위치들이 담긴 배열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, int[][] cors, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, convertIntArrToCoordinateArr(cors), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param row     대체할 행
	 * @param col     대체할 열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int row, int col, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, new CoordinateImpl(row, col), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     대체할 행
	 * @param col     대체할 열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, rowSize, colSize, new CoordinateImpl(row, col), chkChar, startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다.
	 * 
	 * @param arr   출력할 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 * @param start 출력을 시작할 위치
	 */
	public static void printArr(char[] arr, int start, int end, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			System.out.printf("%c ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * char 1차원 배열의 전체를 출력합니다.
	 *
	 * @param arr 출력할 배열
	 */
	public static void printArr(char[] arr, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[] arr, int start, int end, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			if (i == chkIdx)
				System.out.printf("%c", chkChar);
			else
				System.out.printf("%c ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * char 1차원 배열의 전체를 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[] arr, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, chkIdx, chkChar, startText);
	}
	// *******************************************END/char

	// *******************************************START/boolean
	/**
	 * boolean 2차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr 출력할 배열
	 */
	public static void printArr(boolean[][] arr, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, startText);
	}

	/**
	 * boolean 1차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr   출력할 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 */
	public static void printArr(boolean[] arr, int start, int end, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), start, end, startText);
	}

	/**
	 * boolean 1차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[] arr, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), chkIdx, chkChar, startText);
	}

	/**
	 * boolean 1차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[] arr, int start, int end, int chkIdx, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), start, end, chkIdx, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, row, col, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int row, int col, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), row, col, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    출력을 대체할 위치 정보 클래스 배열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, cors, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param cors    출력을 대체할 위치 정보 클래스 배열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[][] arr, CoordinateDebugger[] cors, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), cors, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param cors    출력을 대체할 위치 정보 배열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[][] arr, int[][] cors, char chkChar, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), cors, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    출력을 대체할 위치 정보 배열
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, int[][] cors, char chkChar,
			String... startText) {
		if (!instance.PRINT)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, cors, chkChar, startText);
	}

	/**
	 * boolean 1차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr 출력할 배열
	 */
	public static void printArr(boolean[] arr, String... startText) {
		if (!instance.PRINT)
			return;

		printArr(arr, 0, arr.length, startText);
	}
	// *******************************************END/boolean

	// *******************************************START/etc
	private static boolean chkIgnore(int value, int ignore) {
		if (ignore == Integer.MAX_VALUE) { // ignore값이 없는경우
			if (config.IGNORE_MIN_MAX_VAL)
				return value == Integer.MAX_VALUE || value == Integer.MIN_VALUE;
			else
				return false;
		} else { // ignore값이 특정 값인경우
			if (config.IGNORE_MIN_MAX_VAL)
				return value == ignore || value == Integer.MAX_VALUE || value == Integer.MIN_VALUE;
			else
				return value == ignore;
		}
	}

	private static int[] convertBooleanToIntArr(boolean[] arr) {
		int[] ret = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			ret[i] = arr[i] ? 1 : 0;
		}
		return ret;
	}

	private static int[][] convertBooleanToIntArr(boolean[][] arr) {
		int[][] printArr = new int[arr.length][];
		for (int i = 0; i < arr.length; i++) {
			printArr[i] = convertBooleanToIntArr(arr[i]);
		}
		return printArr;
	}

	private static CoordinateDebugger[] convertCoordinateToArr(CoordinateDebugger cor) {
		CoordinateDebugger[] cors = new CoordinateDebugger[1];
		cors[0] = cor;
		return cors;
	}

	private static CoordinateImpl[] convertIntArrToCoordinateArr(int[][] coordinates) {
		CoordinateImpl[] cors = new CoordinateImpl[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			cors[i] = new CoordinateImpl(coordinates[i][0], coordinates[i][1]);
		}
		return cors;
	}

	private static void printStartText(String[] text) {
		if (config.PRINT_WITH_HR)
			printHR();
		for (String s : text) {
			System.out.printf("%s ", s);
		}
		System.out.println();
	}

	private static void printHRforce() {
		for (int i = 0; i < config.DEFAULT_HR_CNT; i++) {
			System.out.print(config.DEFAULT_HR_CHAR);
		}
		System.out.println();
	}

	private static void printDebugStatus() {
		printHRforce();
		System.out.println("DEBUG : " + instance.PRINT);
		System.out.println("USE_INPUT_FILE : " + instance.USE_INPUT_FILE);
		System.out.println("inputFile : " + instance.inputFile);
		printHRforce();
	}

	public static void getInfo() {
		printHRforce();
		System.out.println("DEBUG : " + instance.PRINT);
		System.out.println("USE_INPUT_FILE : " + instance.USE_INPUT_FILE);
		System.out.println("inputFile : " + instance.inputFile);
		System.out.println("IGNORE_MIN_MAX_VAL : " + config.IGNORE_MIN_MAX_VAL);
		System.out.println("PRINT_WITH_HR : " + config.PRINT_WITH_HR);
		System.out.println("DEFAULT_HR_CHAR : " + config.DEFAULT_HR_CHAR);
		System.out.println("DEFAULT_HR_CNT : " + config.DEFAULT_HR_CNT);
		System.out.println("DEFAULT_IGNORE_CHAR : " + config.IGNORE_CHAR);
		printHRforce();
	}

	/**
	 * 아무 기능도 없지만, debug 모드에서 사용할 수 있는 브레이크포인트 함수입니다.
	 */
	public static void breakPoint() {
		System.out.print("");
	}

	/**
	 * 구분선을 출력합니다.
	 * 
	 * @param c   구분선으로 출력할 문자
	 * @param cnt 구분선으로 출력할 문자의 갯수
	 */
	public static void printHR(char c, int cnt) {
		if (!instance.PRINT)
			return;

		for (int i = 0; i < cnt; i++) {
			System.out.print(c);
		}
		System.out.println();
	}

	/**
	 * 구분선을 출력합니다.
	 * 
	 * @param c 구분선으로 출력할 문자
	 */
	public static void printHR(char c) {
		if (!instance.PRINT)
			return;
		printHR(c, config.DEFAULT_HR_CNT);
	}

	/**
	 * 구분선을 출력합니다.
	 */
	public static void printHR() {
		if (!instance.PRINT)
			return;
		printHR(config.DEFAULT_HR_CHAR, config.DEFAULT_HR_CNT);
	}

	/**
	 * 객체를 출력합니다.
	 * 
	 * @param obj 출력할 객체
	 */
	public static void print(Object... obj) {
		if (!instance.PRINT)
			return;
		for (Object s : obj)
			System.out.println(s.toString());
	}

	/**
	 * 시간측정을 시작합니다.
	 */
	public static void startTimer() {
		if (!instance.PRINT)
			return;
		instance.timer = System.currentTimeMillis();
	}

	/**
	 * startTimer() 함수부터 측정한 시간을 측정하여 출력합니다.
	 */
	public static void chkTimer() {
		long chk = System.currentTimeMillis() - instance.timer;
		if (!instance.PRINT)
			return;
		System.out.println();
		Debug.printHR();
		System.out.println(String.format("코드 실행시간 : %d ms | %f s", chk, (double) (chk / 1000)));
	}
	// *******************************************END/etc
}
