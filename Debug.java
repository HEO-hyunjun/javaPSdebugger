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
		System.out.println("^.*(Debug).*");
		System.out.println("find and replace(ctrl+F)로 위 정규식을 입력후, 공백으로 replace하여 제출해주세요");
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
