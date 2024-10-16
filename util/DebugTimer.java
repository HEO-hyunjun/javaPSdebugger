package javaPSdebugger.util;

import javaPSdebugger.Debug;

public class DebugTimer {
	private static long timer;
	private static final String[] memorySuffix = { "bytes", "KB", "MB", "GB" };

	/**
	 * 시간측정을 시작합니다.
	 */
	public void start() {
		timer = System.currentTimeMillis();
	}

	/**
	 * startTimer() 함수부터 측정한 시간을 측정하여 출력합니다.
	 */
	public void chk() {
		long chk = System.currentTimeMillis() - timer;
		System.out.println();
		Debug.print.hrForce();
		System.out.println(String.format("코드 실행시간 : %d ms | %.2f s", chk, ((double) chk / 1000)));

		Runtime.getRuntime().gc();
		// java vm에 할당된 총 메모리
		long totalMem = Runtime.getRuntime().totalMemory();
		// java vm이 추가로 할당 가능한 메모리
		long freeMem = Runtime.getRuntime().freeMemory();
		// 현재 사용중인 메모리
		long usedMem = totalMem - freeMem;
		
		double convertMem = usedMem;
		int suffixSelect = 0;

		while (convertMem / 1024 > 1 && suffixSelect < 4) {
			convertMem /= 1024;
			suffixSelect++;
		}
		System.out.print(String.format("사용 메모리양 : %d bytes | %.2f %s", usedMem, convertMem, memorySuffix[suffixSelect]));
	}
}
