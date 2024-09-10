package javaPSdebugger;

import java.io.*;

import javaPSdebugger.util.DebugConfigure;
import javaPSdebugger.util.DebugPrint;
import javaPSdebugger.util.DebugTimer;

/**
 * This file was written in UTF-8 format. So, If you can't read Korean letters,
 * change Editor encoding setting to UTF-8
 * 
 * @author HEO-hyunjun
 * 
 */
public class Debug {
	public static DebugConfigure config;
	public static DebugPrint print;
	public static DebugTimer timer;

	private static Debug instance = new Debug();

	private Debug() {
		System.out.println("Debug 모듈이 import됐습니다.");
	}

	/**
	 * 객체를 출력합니다.
	 * 
	 * @param obj 출력할 객체
	 */
	public static void print(Object... obj) {
		if (!Debug.config.PRINT)
			return;
		for (Object s : obj)
			System.out.println(s.toString());
	}

	/**
	 * Debug를 시작합니다.
	 */
	public static void start(Object nowClass) {
		config = new DebugConfigure(nowClass);
		if (config.AUTO_WRITE_SUBMIT_CODE) {
			System.out.printf("자동 제출코드(%s)가 생성됐으니,\n", config.AUTO_SUBMIT_CLASS_NAME);
			System.out.println("그 파일을 그대로 복사해서 제출해주세요.");
		} else {
			System.out.println("^.*(Debug).*");
			System.out.println("find and replace(ctrl+F)로 위 정규식을 입력후,");
			System.out.println("공백으로 변환하고 최종확인 후 제출해주세요.");
		}

		print = new DebugPrint();
		timer = new DebugTimer();
	}

	/**
	 * 아무 기능도 없지만, debug 모드에서 사용할 수 있는 브레이크포인트 함수입니다.
	 */
	public static void breakPoint() {
		System.out.print("");
	}

}
