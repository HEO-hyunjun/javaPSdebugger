package javaPSdebugger.util;

import javaPSdebugger.Debug;

public class DebugTimer {
	private static long timer;
	
	/**
	 * 시간측정을 시작합니다.
	 */
	public void start() {
		if (!Debug.config.PRINT)
			return;
		timer = System.currentTimeMillis();
	}

	/**
	 * startTimer() 함수부터 측정한 시간을 측정하여 출력합니다.
	 */
	public void chk() {
		long chk = System.currentTimeMillis() - timer;
		if (!Debug.config.PRINT)
			return;
		System.out.println();
		Debug.print.hr();
		System.out.println(String.format("코드 실행시간 : %d ms | %f s", chk, (double) (chk / 1000)));
	}
}
