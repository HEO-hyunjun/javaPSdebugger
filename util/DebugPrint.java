package javaPSdebugger.util;

import javaPSdebugger.Debug;

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

public class DebugPrint {
	private final int ALL = Integer.MAX_VALUE;
	private final CoordinateImpl[] NONE_COOR = { new CoordinateImpl(-1, -1) };

	public DebugPrint() {
		hrForce();
		if (Debug.config.SHOW_INFO_DETAIL == DebugConfigure.InfoDetail.DETAIL)
			detailInfo();
		else if (Debug.config.SHOW_INFO_DETAIL == DebugConfigure.InfoDetail.SIMPLE)
			simpleInfo();
	}

	private boolean chkIgnore(int value) {
		if (Debug.config.IGNORE_MIN_MAX_VAL)
			return value == Integer.MAX_VALUE || value == Integer.MIN_VALUE;
		else
			return false;
	}

	private String getFormat(int maxDigit, int object, boolean isChked) {
		if (isChked)
			return ">%" + maxDigit + "d<";
		else
			return " %" + maxDigit + "d ";
	}

	private String getFormat(int maxDigit, char object, boolean isChked) {
		if (isChked)
			return ">%" + maxDigit + "c<";
		else
			return " %" + maxDigit + "c ";
	}

	private String getIDXFormat(int maxDigit, int idx) {
		return String.format("[%" + maxDigit + "d]", idx);
	}

	private int[] getDigitArr(int[][] arr, int rowSize, int colSize) {
		int[] digitArr = new int[colSize];
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < colSize; j++) {
				digitArr[j] = Math.max(digitArr[j], getDigit(arr[i][j]));
				digitArr[j] = Math.max(digitArr[j], getDigit(j));
			}
		}

		return digitArr;
	}

	private int[] getDigitArr(char[][] arr, int rowSize, int colSize) {
		int[] digitArr = new int[colSize];
		for (int i = 0; i < colSize; i++) {
			digitArr[i] = Math.max(1, getDigit(i));
		}
		return digitArr;
	}

	private int[] getSize(int[][] arr, int rowSize, int colSize) {
		rowSize = Math.min(rowSize, arr.length);

		int maxColSize = 0;
		for (int i = 0; i < arr.length; i++) {
			maxColSize = Math.max(maxColSize, arr[i].length);
		}
		colSize = Math.min(maxColSize, colSize);
		return new int[] { rowSize, colSize };
	}

	private int[] getSize(char[][] arr, int rowSize, int colSize) {
		rowSize = Math.min(rowSize, arr.length);

		int maxColSize = 0;
		for (int i = 0; i < arr.length; i++) {
			maxColSize = Math.max(maxColSize, arr[i].length);
		}
		colSize = Math.min(maxColSize, colSize);
		return new int[] { rowSize, colSize };
	}

	private String getColIdxString(int[][] arr, int rowSize, int colSize) {
		if (!Debug.config.PRINT_WITH_INDEX)
			return "";
		StringBuilder ret = new StringBuilder();
		int[] size = getSize(arr, rowSize, colSize);
		rowSize = size[0];
		colSize = size[1];

		int[] digitArr = getDigitArr(arr, rowSize, colSize);

		int frontBlank = getDigit(rowSize - 1) + 2;
		for (int i = 0; i < frontBlank; i++)
			ret.append(' ');
		for (int i = 0; i < colSize; i++) {
			ret.append(getIDXFormat(digitArr[i], i));
		}

		return ret.append('\n').toString();
	}

	private String getColIdxString(char[][] arr, int rowSize, int colSize) {
		if (!Debug.config.PRINT_WITH_INDEX)
			return "";
		StringBuilder ret = new StringBuilder();
		int[] size = getSize(arr, rowSize, colSize);
		rowSize = size[0];
		colSize = size[1];

		int[] digitArr = getDigitArr(arr, rowSize, colSize);

		int frontBlank = getDigit(rowSize - 1) + 2;
		for (int i = 0; i < frontBlank; i++)
			ret.append(' ');
		for (int i = 0; i < colSize; i++) {
			ret.append(getIDXFormat(digitArr[i], i));
		}

		return ret.append('\n').toString();
	}

	private int getDigit(int n) {
		if (n == 0 || chkIgnore(n))
			return 1;

		int ret = 0;
		if (n < 0)
			ret++;

		n = Math.abs(n);
		while (n > 0) {
			n /= 10;
			ret++;
		}
		return ret;
	}

	private int[] convertBooleanToIntArr(boolean[] array) {
		int[] ret = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			ret[i] = array[i] ? 1 : 0;
		}
		return ret;
	}

	private int[][] convertBooleanToIntArr(boolean[][] array) {
		int[][] arr = new int[array.length][];
		for (int i = 0; i < array.length; i++) {
			arr[i] = convertBooleanToIntArr(array[i]);
		}
		return arr;
	}

	private boolean[][] convert2D(boolean[] array, int start, int end) {
		start = Math.max(start, 0);
		end = Math.min(end, array.length);
		if (start > end)
			return new boolean[0][0];

		boolean[][] newArray = new boolean[1][end - start];
		int cnt = 0;
		for (int i = start; i < end; i++)
			newArray[0][cnt++] = array[i];
		return newArray;
	}

	private char[][] convert2D(char[] array, int start, int end) {
		start = Math.max(start, 0);
		end = Math.min(end, array.length);
		if (start > end)
			return new char[0][0];

		char[][] newArray = new char[1][end - start];
		int cnt = 0;
		for (int i = start; i < end; i++)
			newArray[0][cnt++] = array[i];
		return newArray;
	}

	private int[][] convert2D(int[] array, int start, int end) {
		start = Math.max(start, 0);
		end = Math.min(end, array.length);
		if (start > end)
			return new int[0][0];

		int[][] newArray = new int[1][end - start];
		int cnt = 0;
		for (int i = start; i < end; i++)
			newArray[0][cnt++] = array[i];
		return newArray;
	}

	private CoordinateDebugger[] convertCoordinateArr(CoordinateDebugger cor) {
		CoordinateDebugger[] cors = new CoordinateDebugger[1];
		cors[0] = cor;
		return cors;
	}

	private CoordinateDebugger[] convertCoordinateArr(int[][] coordinates) {
		CoordinateImpl[] cors = new CoordinateImpl[coordinates.length];
		for (int i = 0; i < coordinates.length; i++) {
			cors[i] = new CoordinateImpl(coordinates[i][0], coordinates[i][1]);
		}
		return cors;
	}

	private CoordinateDebugger[] convertCoordinateArr(int row, int col) {
		return convertCoordinateArr(new CoordinateImpl(row, col));
	}

	/**
	 * boolean 1차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(boolean[] array, int chkIdx, String... startText) {
		arr(convert2D(array, 0, ALL), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * boolean 1차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(boolean[] array, int start, int end, int chkIdx, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * boolean 1차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array 출력할 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 */
	public void arr(boolean[] array, int start, int end, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * boolean 1차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array 출력할 배열
	 */
	public void arr(boolean[] array, String... startText) {
		arr(convert2D(array, 0, ALL), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array 출력할 배열
	 * @param cors  출력을 대체할 위치 정보 클래스 배열
	 */
	public void arr(boolean[][] array, CoordinateDebugger[] cors, String... startText) {
		arr(array, ALL, ALL, cors, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public void arr(boolean[][] array, int[] cor, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	// boolean Impl
	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    출력을 대체할 위치 정보 클래스 배열
	 */
	public void arr(boolean[][] array, int rowSize, int colSize, CoordinateDebugger[] cors, String... startText) {
		arr(convertBooleanToIntArr(array), rowSize, colSize, cors, startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public void arr(boolean[][] array, int rowSize, int colSize, int[] cor, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    출력을 대체할 위치 정보 배열
	 */
	public void arr(boolean[][] array, int rowSize, int colSize, int[][] cors, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cors), startText);
	}

	/**
	 * boolean 2차원 배열의 일부분을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public void arr(boolean[][] array, int rowSize, int colSize, String... startText) {
		arr(array, rowSize, colSize, NONE_COOR, startText);
	}

	/**
	 * boolean 2차원 배열의 전체를 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array 출력할 배열
	 * @param cors  출력을 대체할 위치 정보 배열
	 */
	public void arr(boolean[][] array, int[][] cors, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cors), startText);
	}

	/**
	 * boolean 2차원 배열을 출력합니다. true는 1로, false는 0으로 출력합니다.
	 *
	 * @param array 출력할 배열
	 */
	public void arr(boolean[][] array, String... startText) {
		arr(array, ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * char 1차원 배열의 전체를 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(char[] array, int chkIdx, String... startText) {
		arr(convert2D(array, 0, ALL), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(char[] array, int start, int end, int chkIdx, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * char 1차원 배열의 일부분을 출력합니다.
	 * 
	 * @param array 출력할 배열
	 * @param start 출력을 시작할 위치
	 * @param end   출력을 종료할 위치 (exclusive)
	 * @param start 출력을 시작할 위치
	 */
	public void arr(char[] array, int start, int end, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * char 1차원 배열의 전체를 출력합니다.
	 *
	 * @param array 출력할 배열
	 */
	public void arr(char[] array, String... startText) {
		arr(convert2D(array, 0, array.length), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array 출력할 배열
	 * @param cor   특정 위치가 담긴 클래스
	 */
	public void arr(char[][] array, CoordinateDebugger cor, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cor), startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값들을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array 출력할 배열
	 * @param cors  특정 위치들이 담긴 클래스
	 */
	public void arr(char[][] array, CoordinateDebugger[] cors, String... startText) {
		arr(array, ALL, ALL, cors, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array 출력할 배열
	 * @param row   대체할 행
	 * @param col   대체할 열
	 */
	public void arr(char[][] array, int[] cor, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치가 담긴 클래스
	 */
	public void arr(char[][] array, int rowSize, int colSize, CoordinateDebugger cor, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cor), startText);
	}

	// char impl
	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정 위치의 값들을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    특정 위치들이 담긴 클래스
	 */
	public void arr(char[][] array, int rowSize, int colSize, CoordinateDebugger[] cors, String... startText) {
		if (!Debug.config.PRINT)
			return;

		printStartText(startText);
		StringBuilder sb = new StringBuilder();

		int[] size = getSize(array, rowSize, colSize);
		rowSize = size[0];
		colSize = size[1];
		int[] digitArr = getDigitArr(array, rowSize, colSize);

		int rowMaxDigit = getDigit(rowSize - 1);
		sb.append(getColIdxString(array, rowSize, colSize));
		for (int i = 0; i < rowSize; i++) {
			if (Debug.config.PRINT_WITH_INDEX)
				sb.append(getIDXFormat(rowMaxDigit, i));
			int newColSize = Math.min(colSize, array[i].length);

			for (int j = 0; j < newColSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}

				if (chkIgnore(array[i][j]))
					sb.append(String.format(getFormat(digitArr[j], Debug.config.IGNORE_CHAR, flag),
							Debug.config.IGNORE_CHAR));
				else
					sb.append(String.format(getFormat(digitArr[j], array[i][j], flag), array[i][j]));
			}
			sb.append('\n');
		}
		System.out.println(sb);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public void arr(char[][] array, int rowSize, int colSize, int[] cor, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cors    특정 위치들이 담긴 배열
	 */
	public void arr(char[][] array, int rowSize, int colSize, int[][] cors, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cors), startText);
	}

	/**
	 * char 2차원 배열의 일부분을 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 */
	public void arr(char[][] array, int rowSize, int colSize, String... startText) {
		arr(array, rowSize, colSize, NONE_COOR, startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다. 특정위치의 값을 chkChar로 대체하여 출력합니다.
	 * 
	 * @param array   출력할 배열
	 * @param cors    특정 위치들이 담긴 클래스
	 * @param chkChar 출력을 대체할 문자
	 */
	public void arr(char[][] array, int[][] cors, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cors), startText);
	}

	/**
	 * char 2차원 배열의 전체를 출력합니다.
	 * 
	 * @param array 출력할 배열
	 */
	public void arr(char[][] array, String... startText) {
		arr(array, ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * int 1차원 배열의 전체를 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(int[] array, int chkIdx, String... startText) {
		arr(convert2D(array, 0, ALL), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. 특정 위치의 값을 chkChar로 대체하여 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param chkIdx 출력을 대체할 위치
	 */
	public void arr(int[] array, int start, int end, int chkIdx, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, convertCoordinateArr(0, chkIdx), startText);
	}

	/**
	 * int 1차원 배열의 일부분을 출력합니다. ignore 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param start  출력을 시작할 위치
	 * @param end    출력을 종료할 위치 (exclusive)
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public void arr(int[] array, int start, int end, String... startText) {
		arr(convert2D(array, start, end), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * int 1차원 배열을 출력합니다.
	 *
	 * @param array 출력할 배열
	 */
	public void arr(int[] array, String... startText) {
		arr(convert2D(array, 0, ALL), ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param array 출력할 배열
	 * @param cor   특정 위치 좌표가 포함된 클래스
	 */
	public void arr(int[][] array, CoordinateDebugger cor, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cor), startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 */
	public void arr(int[][] array, CoordinateDebugger[] cors, String... startText) {
		arr(array, ALL, ALL, cors, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param array  출력할 배열
	 * @param row    문자를 대체할 행 인덱스
	 * @param col    문자를 대체할 열 인덱스
	 * @param ignore 무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public void arr(int[][] array, int[] cor, String... startText) {
		arr(array, ALL, ALL, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 */
	public void arr(int[][] array, int rowSize, int colSize, CoordinateDebugger cor, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cor), startText);
	}

	// int Impl
	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param cor     특정 위치 좌표가 포함된 클래스
	 */
	public void arr(int[][] array, int rowSize, int colSize, CoordinateDebugger[] cors, String... startText) {
		if (!Debug.config.PRINT)
			return;

		printStartText(startText);
		StringBuilder sb = new StringBuilder();

		int[] size = getSize(array, rowSize, colSize);
		rowSize = size[0];
		colSize = size[1];
		int[] digitArr = getDigitArr(array, rowSize, colSize);

		int rowMaxDigit = getDigit(rowSize - 1);
		sb.append(getColIdxString(array, rowSize, colSize));
		for (int i = 0; i < rowSize; i++) {
			if (Debug.config.PRINT_WITH_INDEX)
				sb.append(getIDXFormat(rowMaxDigit, i));
			int newColSize = Math.min(colSize, array[i].length);

			for (int j = 0; j < newColSize; j++) {
				boolean flag = false;
				for (int k = 0; k < cors.length && cors[k] != null; k++) {
					CoordinateDebugger cor = cors[k];
					if (i == cor.getRow() && j == cor.getCol()) {
						flag = true;
						break;
					}
				}

				if (chkIgnore(array[i][j]))
					sb.append(String.format(getFormat(digitArr[j], Debug.config.IGNORE_CHAR, flag),
							Debug.config.IGNORE_CHAR));
				else
					sb.append(String.format(getFormat(digitArr[j], array[i][j], flag), array[i][j]));
			}
			sb.append('\n');
		}
		System.out.println(sb);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param row     문자를 대체할 행 인덱스
	 * @param col     문자를 대체할 열 인덱스
	 */
	public void arr(int[][] array, int rowSize, int colSize, int[] cor, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(cor[0], cor[1]), startText);
	}

	/**
	 * int 2차원 배열의 일부분을 출력합니다. 지정된 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param array   출력할 배열
	 * @param rowSize 출력할 행의 수
	 * @param colSize 출력할 열의 수
	 * @param ignore  무시할 값. 이 값과 같은 경우 DEFAULT_IGNORE_CHAR로 출력됩니다.
	 */
	public void arr(int[][] array, int rowSize, int colSize, String... startText) {
		arr(array, rowSize, colSize, NONE_COOR, startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param array       출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 */
	public void arr(int[][] array, int rowSize, int colSize, int[][] coordinates, String... startText) {
		arr(array, rowSize, colSize, convertCoordinateArr(coordinates), startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 특정 위치에 있는 값을 지정된 문자로 대체하여 출력하고, 지정된 값과 같은 경우
	 * DEFAULT_IGNORE_CHAR로 출력합니다.
	 *
	 * @param array       출력할 배열
	 * @param coordinates 대체할 문자가 담긴 int배열 [idx][row=0, col=1] 형태로만 가능합니다.
	 */
	public void arr(int[][] array, int[][] coordinates, String... startText) {
		arr(array, convertCoordinateArr(coordinates), startText);
	}

	/**
	 * int 2차원 배열을 출력합니다. 디버그 모드가 활성화된 경우에만 배열을 출력합니다.
	 *
	 * @param array 출력할 배열
	 */
	public void arr(int[][] array, String... startText) {
		arr(array, ALL, ALL, NONE_COOR, startText);
	}

	/**
	 * 구분선을 출력합니다.
	 */
	public void hr() {
		hr(Debug.config.DEFAULT_HR_CHAR, Debug.config.DEFAULT_HR_CNT);
	}

	/**
	 * 구분선을 출력합니다.
	 * 
	 * @param c 구분선으로 출력할 문자
	 */
	public void hr(char c) {
		hr(c, Debug.config.DEFAULT_HR_CNT);
	}

	/**
	 * 구분선을 출력합니다.
	 * 
	 * @param c   구분선으로 출력할 문자
	 * @param cnt 구분선으로 출력할 문자의 갯수
	 */
	public void hr(char c, int cnt) {
		if (!Debug.config.PRINT)
			return;

		for (int i = 0; i < cnt; i++) {
			System.out.print(c);
		}
		System.out.println();
	}

	/**
	 * 구분선을 설정과 상관없이 강제로 출력합니다.
	 * 
	 */
	public void hrForce() {
		for (int i = 0; i < Debug.config.DEFAULT_HR_CNT; i++) {
			System.out.print(Debug.config.DEFAULT_HR_CHAR);
		}
		System.out.println();
	}

	private void printStartText(String[] text) {
		if (Debug.config.PRINT_WITH_HR)
			hr();
		for (String s : text) {
			System.out.printf("%s ", s);
		}
		System.out.println();
	}

	public void simpleInfo() {
		System.out.println("현재 클래스 : " + Debug.config.DEBUG_CLASS);
		System.out.println("현재 파일 : " + Debug.config.DEBUG_JAVA_FILE);
		System.out.println("PRINT : " + Debug.config.PRINT);
		
		System.out.println("USE_INPUT_FILE : " + Debug.config.USE_INPUT_FILE);
		if (Debug.config.USE_INPUT_FILE)
			System.out.println("\tINPUT_FILE : " + Debug.config.INPUT_FILE);

		System.out.println("AUTO_WRITE_SUBMIT_CODE : " + Debug.config.AUTO_WRITE_SUBMIT_CODE);

		if (Debug.config.AUTO_WRITE_SUBMIT_CODE) {
			System.out.println("\tAUTO_SUBMIT_CLASS_NAME : " + Debug.config.AUTO_SUBMIT_CLASS_NAME);
			if (!Debug.config.AUTO_WRITE_JAVA_FILE)
				System.out.println("\tAUTO_SUBMIT_FILE_NAME : " + Debug.config.AUTO_SUBMIT_FILE_NAME);
		}
		hrForce();
	}

	public void detailInfo() {		
		System.out.println("현재 프로젝트 경로 : " + Debug.config.ROOT_PATH);
		System.out.println("Debug.java 경로 : " + Debug.config.DEBUG_PACKAGE_PATH);
		System.out.println("현재 클래스 : " + Debug.config.DEBUG_CLASS);
		System.out.println("현재 파일 : " + Debug.config.DEBUG_JAVA_FILE);
		System.out.println("PRINT : " + Debug.config.PRINT);
		System.out.println("USE_INPUT_FILE : " + Debug.config.USE_INPUT_FILE);
		if (Debug.config.USE_INPUT_FILE)
			System.out.println("\tINPUT_FILE : " + Debug.config.INPUT_FILE);

		System.out.println("AUTO_WRITE_SUBMIT_CODE : " + Debug.config.AUTO_WRITE_SUBMIT_CODE);

		if (Debug.config.AUTO_WRITE_SUBMIT_CODE) {
			System.out.println("\tAUTO_SUBMIT_CLASS_NAME : " + Debug.config.AUTO_SUBMIT_CLASS_NAME);
			System.out.println("\tAUTO_WRITE_JAVA_FILE : " + Debug.config.AUTO_WRITE_JAVA_FILE);
			if (!Debug.config.AUTO_WRITE_JAVA_FILE)
				System.out.println("\tAUTO_SUBMIT_FILE_NAME : " + Debug.config.AUTO_SUBMIT_FILE_NAME);
		}

		System.out.println("IGNORE_MIN_MAX_VAL : " + Debug.config.IGNORE_MIN_MAX_VAL);
		if (Debug.config.IGNORE_MIN_MAX_VAL)
			System.out.println("\tIGNORE_CHAR : " + Debug.config.IGNORE_CHAR);

		System.out.println("PRINT_WITH_HR : " + Debug.config.PRINT_WITH_HR);
		if(Debug.config.PRINT_WITH_HR)
			System.out.println("\tDEFAULT_HR_CHAR : " + Debug.config.DEFAULT_HR_CHAR);
			System.out.println("\tDEFAULT_HR_CNT : " + Debug.config.DEFAULT_HR_CNT);
		hrForce();
	}

}
