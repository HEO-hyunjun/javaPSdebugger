package javaPSdebugger.util;

public interface CoordinateDebugger {
	/**
	 * 위치정보를 클래스로 작성했을때 Debug 클래스에 넘겨줄 행 정보를 반환합니다.
	 * 
	 * @return 반환할 행
	 */
	int getRow();

	/**
	 * 위치정보를 클래스로 작성했을때 Debug 클래스에 넘겨줄 열 정보를 반환합니다.
	 * 
	 * @return 반환할 열
	 */
	int getCol();
}
