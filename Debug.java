package javaPSdebugger;

import java.io.*;

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

public class Debug {
	private static Debug instance = new Debug();
	private String inputFile = "input.txt";
	private boolean DEBUG = false;
	private boolean USE_INPUT_FILE = false;
	private boolean IGNORE_MIN_MAX_VAL = true;
	private boolean PRINT_WITH_HR = true;
	private long timer;
	public static char DEFAULT_IGNORE_CHAR = '&';
	public static char DEFAULT_HR_CHAR = '*';
	public static int DEFAULT_HR_CNT = 45;

	private Debug() {
		System.out.println("Debug 모듈이 import됐습니다.");
		System.out.println("^.*(Debug).*");
		System.out.println("find and replace(ctrl+F)로 위 정규식을 입력후, 공백으로 replace하여 제출해주세요");
	}

	// **************************************************************************START/setting
	/**
	 * 디버그 모드를 설정합니다. 디버그 모드가 활성화되면 지정된 입력 파일을 사용합니다.
	 *
	 * @param debug 디버그 모드를 활성화하려면 true, 비활성화하려면 false
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setDebug(boolean debug) throws IOException {
		printDebugStatus();
		instance.DEBUG = debug;
		if (!instance.DEBUG)
			return;
		setInputFile(instance.inputFile);
	}

	/**
	 * 디버그 모드를 설정하고, 입력 파일 사용 여부를 결정합니다.
	 *
	 * @param debug    디버그 모드를 활성화하려면 true, 비활성화하려면 false
	 * @param useInput 입력 파일을 사용할지 여부를 결정합니다.
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setDebug(boolean debug, boolean useInput) throws IOException {
		printDebugStatus();
		instance.DEBUG = debug;
		setInputFile(useInput);
	}

	/**
	 * 디버그 모드를 설정하고, 입력 파일 사용 여부를 결정합니다.
	 *
	 * @param debug     디버그 모드를 활성화하려면 true, 비활성화하려면 false
	 * @param inputFile 입력 파일을 사용할지 여부를 결정합니다.
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setDebug(boolean debug, boolean useInput, String inputFile) throws IOException {
		printDebugStatus();
		instance.DEBUG = debug;
		instance.USE_INPUT_FILE = useInput;
		setInputFile(inputFile);
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

	/**
	 * 입력 파일 경로를 설정합니다. 설정 후, 입력 파일 사용 여부에 따라 입력 스트림을 대체합니다.
	 *
	 * @param inputFile 사용할 입력 파일의 경로
	 * @throws IOException 파일 입출력 오류가 발생할 수 있습니다.
	 */
	public static void setInputFile(String inputFile) throws IOException {
		instance.inputFile = inputFile;
	}

	/**
	 * 구분선 출력을 설정합니다.
	 * 
	 * @param set 구분선을 사용 여부
	 */
	public static void setHR(boolean set) {
		instance.PRINT_WITH_HR = set;
	}

	/**
	 * int배열을 출력할때 MAX,MIN_VALUE를 무시하고 0을 출력 여부를 설정합니다.
	 * 
	 * @param set 무시 여부
	 */
	public static void setIgnoreMaxMin(boolean set) {
		instance.IGNORE_MIN_MAX_VAL = set;
	}

	/**
	 * 시간측정을 시작합니다.
	 */
	public static void startTimer() {
		if (!instance.DEBUG)
			return;
		instance.timer = System.currentTimeMillis();
	}

	/**
	 * startTimer() 함수부터 측정한 시간을 측정하여 출력합니다.
	 */
	public static void chkTimer() {
		long chk = instance.timer - System.currentTimeMillis();
		if (!instance.DEBUG)
			return;

		System.out.println(String.format("코드 실행시간 : %d ms | %d s", chk, chk / 1000));
	}
	// **************************************************************************END/start

	// **************************************************************************START/printArr
	// *******************************************START/int[][]
	// *******************************************START/actualFunction
	/**
	 * int 2차원 배열을 출력합니다. 지정된 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr    출력할 int 2차원 배열
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
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
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
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
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}
				if (flag)
					System.out.printf("%c ", chkChar);
				else if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
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
	 * @param arr     출력할 int 2차원 배열
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, CoordinateDebugger[] cors, char chkChar, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}
				if (flag)
					System.out.printf("%c ", chkChar);
				else if (chkIgnore(arr[i][j], ignore))
					System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
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
	 * @param arr 출력할 int 2차원 배열
	 */
	public static void printArr(int[][] arr, String... startText) {
		if (!instance.DEBUG)
			return;
		printArr(arr, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 디버그 모드가 활성화된 경우에만 배열을 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, row, col, chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int row, int col, char chkChar, int ignore,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, new CoordinateImpl(row, col), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int row, int col, char chkChar, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, new CoordinateImpl(row, col), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 int 2차원 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     대체할 문자
	 * @param ignore      무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int[][] coordinates, char chkChar, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 int 2차원 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     대체할 문자
	 */
	public static void printArr(int[][] arr, int[][] coordinates, char chkChar, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 int 2차원 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     대체할 문자
	 * @param ignore      무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int[][] coordinates, char chkChar, int ignore,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, convertIntArrToCoordinateArr(coordinates), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr         출력할 int 2차원 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 * @param chkChar     대체할 문자
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, int[][] coordinates, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, convertIntArrToCoordinateArr(coordinates), chkChar, Integer.MAX_VALUE,
				startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, int rowSize, int colSize, CoordinateDebugger cor, char chkChar, int ignore,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, convertCoordinateToArr(cor), chkChar, ignore, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 2차원 배열
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 * @param chkChar 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[][] arr, CoordinateDebugger cor, char chkChar, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, convertCoordinateToArr(cor), chkChar, ignore, startText);
	}

	// *******************************************END/int[][]

	// *******************************************START/int[]
	/**
	 * int 1차원 배열을 출력합니다.
	 *
	 * @param arr 출력할 int 1차원 배열
	 */
	public static void printArr(int[] arr, String... startText) {
		if (!instance.DEBUG)
			return;
		printArr(arr, 0, arr.length, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열을 출력합니다. 배열의 값이 ignore 값과 같다면, 0으로 출력합니다.
	 *
	 * @param arr    출력할 int 1차원 배열
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, 0, arr.length, ignore, startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다.
	 *
	 * @param arr   출력할 int 1차원 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 */
	public static void printArr(int[] arr, int start, int end, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			if (chkIgnore(arr[i], ignore))
				System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
			else
				System.out.printf("%d ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. ignore 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr    출력할 int 1차원 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int start, int end, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, start, end, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다. ignore 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 int 1차원 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int start, int end, int chkIdx, char chkChar, int ignore,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = start; i < end; i++) {
			if (i == chkIdx)
				System.out.printf("%c ", chkChar);
			else if (chkIgnore(arr[i], ignore))
				System.out.printf("%c ", DEFAULT_IGNORE_CHAR);
			else
				System.out.printf("%d ", arr[i]);
		}
		System.out.println('\n');
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 1차원 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[] arr, int start, int end, int chkIdx, char chkChar, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, start, end, chkIdx, chkChar, Integer.MAX_VALUE, startText);
	}

	/**
	 * int 1차원 배열의 전체 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다. ignore 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param arr     출력할 int 1차원 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public static void printArr(int[] arr, int chkIdx, char chkChar, int ignore, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, 0, arr.length, ignore, startText);
	}

	/**
	 * int 1차원 배열의 전체를 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 int 1차원 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(int[] arr, int chkIdx, char chkChar, String... startText) {
		if (!instance.DEBUG)
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
		if (!instance.DEBUG)
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
		if (!instance.DEBUG)
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, CoordinateDebugger[] cors, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length; k++) {
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, CoordinateDebugger[] cors, char chkChar, String... startText) {
		if (!instance.DEBUG)
			return;

		printStartText(startText);

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length; k++) {
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, CoordinateDebugger cor, char chkChar, String... startText) {
		if (!instance.DEBUG)
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, CoordinateDebugger cor, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, convertCoordinateToArr(cor), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param cors    특정 위치들이 담긴 클래스
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int[][] cors, char chkChar, String... startText) {
		if (!instance.DEBUG)
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, int[][] cors, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, convertIntArrToCoordinateArr(cors), chkChar, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param arr     출력할 배열
	 * @param row     대체할 행
	 * @param col     대체할 열
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int row, int col, char chkChar, String... startText) {
		if (!instance.DEBUG)
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
	 * @param chkChar 대체할 문자
	 */
	public static void printArr(char[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, rowSize, colSize, new CoordinateImpl(row, col), chkChar, startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다.
	 * 
	 * @param arr
	 * @param start
	 * @param end
	 * @param startText
	 */
	public static void printArr(char[] arr, int start, int end, String... startText) {
		if (!instance.DEBUG)
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
	 * @param arr 출력할 char 1차원 배열
	 */
	public static void printArr(char[] arr, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, 0, arr.length, startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param arr     출력할 char 1차원 배열
	 * @param start   출력을 시작할 위치
	 * @param end     출력을 종료할 위치 (exclusive)
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[] arr, int start, int end, int chkIdx, char chkChar, String... startText) {
		if (!instance.DEBUG)
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
	 * @param arr     출력할 char 1차원 배열
	 * @param chkIdx  출력을 대체할 위치
	 * @param chkChar 출력을 대체할 문자
	 */
	public static void printArr(char[] arr, int chkIdx, char chkChar, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, 0, arr.length, chkIdx, chkChar, startText);
	}
	// *******************************************END/char

	// *******************************************START/boolean
	/**
	 * boolean 2차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr 출력할 boolean 2차원 배열
	 */
	public static void printArr(boolean[][] arr, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(convertBooleanToIntArr(arr), startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 boolean 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, startText);
	}

	/**
	 * boolean 1차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr   출력할 boolean 1차원 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 */
	public static void printArr(boolean[] arr, int start, int end, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(convertBooleanToIntArr(arr), start, end, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 boolean 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int rowSize, int colSize, int row, int col, char chkChar,
			String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(convertBooleanToIntArr(arr), rowSize, colSize, row, col, chkChar, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr     출력할 boolean 2차원 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public static void printArr(boolean[][] arr, int row, int col, char chkChar, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(convertBooleanToIntArr(arr), row, col, chkChar, startText);
	}

	/**
	 * boolean 1차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param arr 출력할 boolean 1차원 배열
	 */
	public static void printArr(boolean[] arr, String... startText) {
		if (!instance.DEBUG)
			return;

		printArr(arr, 0, arr.length, startText);
	}
	// *******************************************END/boolean

	// *******************************************START/etc
	private static boolean chkIgnore(int value, int ignore) {
		if (ignore == Integer.MAX_VALUE) { // ignore값이 없는경우
			if (instance.IGNORE_MIN_MAX_VAL)
				return value == Integer.MAX_VALUE || value == Integer.MIN_VALUE;
			else
				return false;
		} else { // ignore값이 특정 값인경우
			if (instance.IGNORE_MIN_MAX_VAL)
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
		CoordinateDebugger[] cors = new CoordinateImpl[1];
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
		if (instance.PRINT_WITH_HR)
			printHR();
		for (String s : text) {
			System.out.printf("%s ", s);
		}
		System.out.println();
	}

	private static void printHRforce() {
		for (int i = 0; i < DEFAULT_HR_CNT; i++) {
			System.out.print(DEFAULT_HR_CHAR);
		}
		System.out.println();
	}

	private static void printDebugStatus() {
		printHRforce();
		System.out.println("DEBUG : " + instance.DEBUG);
		System.out.println("USE_INPUT_FILE : " + instance.USE_INPUT_FILE);
		System.out.println("inputFile : " + instance.inputFile);
		printHRforce();
	}

	public static void getInfo() {
		printHRforce();
		System.out.println("DEBUG : " + instance.DEBUG);
		System.out.println("USE_INPUT_FILE : " + instance.USE_INPUT_FILE);
		System.out.println("inputFile : " + instance.inputFile);
		System.out.println("IGNORE_MIN_MAX_VAL : " + instance.IGNORE_MIN_MAX_VAL);
		System.out.println("PRINT_WITH_HR : " + instance.PRINT_WITH_HR);
		System.out.println("DEFAULT_HR_CHAR : " + DEFAULT_HR_CHAR);
		System.out.println("DEFAULT_HR_CNT : " + DEFAULT_HR_CNT);
		System.out.println("DEFAULT_IGNORE_CHAR : " + DEFAULT_IGNORE_CHAR);
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
		if (!instance.DEBUG)
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
		if (!instance.DEBUG)
			return;
		printHR(c, DEFAULT_HR_CNT);
	}

	/**
	 * 구분선을 출력합니다.
	 */
	public static void printHR() {
		if (!instance.DEBUG)
			return;
		printHR(DEFAULT_HR_CHAR, DEFAULT_HR_CNT);
	}

	/**
	 * 문자열을 출력합니다.
	 * 
	 * @param s 출력할 문자열
	 */
	public static void print(String s) {
		if (!instance.DEBUG)
			return;

		System.out.println(s);
	}
	// *******************************************END/etc
}
