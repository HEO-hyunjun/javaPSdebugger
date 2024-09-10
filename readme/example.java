package javaPSdebugger.readme;
import java.io.*;
import java.util.StringTokenizer;

import javaPSdebugger.Debug;
import javaPSdebugger.util.CoordinateDebugger;

// 사용자 정의 클래스입니다.
// 특정 위치를 나타내는 클래스입니다.
// auto_write_submit_code에서 CoordinateDebugger 부분만 삭제됩니다.
class PointTest implements CoordinateDebugger {
	int y, x;

	PointTest(int y, int x) {
		this.y = y;
		this.x = x;
	}

	// 이 클래스에서 행을 의미하는 변수를 반환합니다.
	@Override
	public int getRow() {
		return y;
	}

	// 이 클래스에서 열을 의미하는 변수를 반환합니다.
	@Override
	public int getCol() {
		return x;
	}
}

// 사용자 정의 클래스입니다.
// auto_write_submit_code에서 CoordinateDebugger 부분만 삭제됩니다.
class Edge implements CoordinateDebugger, Comparable<Edge> {
	int from, to, weight;

	// 이 클래스에서 행을 의미하는 변수를 반환합니다.
	@Override
	public int getRow() {
		return from;
	}

	// 이 클래스에서 열을 의미하는 변수를 반환합니다.
	@Override
	public int getCol() {
		return to;
	}

	// Comparable의 메서드
	@Override
	public int compareTo(Edge o) {
		return Integer.compare(weight, o.weight);
	}
}

public class example {
	static final int SIZE = 15;
	static StringBuilder ans = new StringBuilder();
	static int[][] testArray = new int[SIZE][SIZE];

	// 깊이우선 탐색 예제입니다.
	static void dfs(int now, int size) {
		for (int i = 0; i < size; i++) {
//			Debug.print.arr(testArray, new int[] { now, i }, "현재 보는 위치(now, i) / 좌표배열(전체)");
//			Debug.breakPoint();
			
			if (testArray[now][i] != 0) {
				// debug 라고만 적으면 한줄이 통째로 사라집니다.
				// 안적은 줄은 남아있겠죠?
				Debug.print.arr(testArray, size, size, new PointTest(now, i), "현재 보는 위치(now, i) / 사용자 정의 클래스(일부분)");
				Debug.print.arr(testArray, size, size, new int[] { now, i }, "현재 보는 위치(now, i) / 좌표배열(일부분)");
				Debug.print.arr(testArray, new PointTest(now, i), "현재 보는 위치(now, i) / 사용자 정의 클래스(전체)");
				Debug.print.arr(testArray, new int[] { now, i }, "현재 보는 위치(now, i) / 좌표배열(전체)");
				Debug.breakPoint();
				ans.append(i).append(' ');
				dfs(i, size);
			}
		}
	}
	
	// 다익스트라 예제입니다.
	static int[] dijkstra(int start, int size) {
		int[] dist = new int[SIZE];
		boolean[] visit = new boolean[SIZE];
		for (int i = 0; i < size; i++) {
			dist[i] = Integer.MAX_VALUE; // Config / IGNORE_MIN_MAX_VAL = true라면 &로 표시됩니다.
		}
		dist[start] = 0;
		visit[start] = true;
		// 1차원 배열도 출력할 수 있습니다.
		// Debug.print.arr(배열, 시작위치, 끝위치)
		// 마지막 배열 출력전 메세지는 여러개를 출력할 수 있습니다.
		Debug.print.arr(dist, 0, size, "1차원 dist 배열 출력", "출력해보기");
		Debug.breakPoint();
		// 없어도 가능하고요
		Debug.print.arr(dist);
		Debug.print.arr(dist, 0, size);
		Debug.breakPoint();

		// boolean도 출력해줍니다.
		Debug.print.arr(visit, 0, size, "boolean 테스트");
		Debug.breakPoint();

		int now = start;
		while (true) {
			Debug.print.arr(visit, now, "now visit");
			for (int i = 0; i < size; i++) {
				if (!visit[i] && testArray[now][i] != 0) {
//					Debug.print.arr(testArray, size, size, new int[] { now, i }, " 갱신할 값");
					dist[i] = Math.min(dist[i], testArray[now][i]);
				}
			}
			Debug.print.arr(dist, 0, size, "dist 갱신");
			Debug.breakPoint();

			int minIdx = -1;
			int min = Integer.MAX_VALUE;
			for (int i = 0; i < size; i++) {
				if (!visit[i] && min > dist[i]) {
					min = dist[i];
					minIdx = i;
				}
			}
			Debug.print.hr(); // 구분선을 출력합니다.
			// Debug.print(객체1, 객체2, ...)
			Debug.print("PrintTest", String.format("min : %d, minIdx : %d", min, minIdx)); // 객체를 출력할수도 있습니다.
			Debug.breakPoint();

			if (minIdx == -1)
				break;

			now = minIdx;
			visit[now] = true;
		}
		Debug.print.arr(dist, 0, size, "최종 dist배열");
		return dist;
	}

	public static void main(String[] args) throws IOException {
		Debug.start(new example()); // main함수가 들어있는 들어있는 인스턴스를 매개변수로 넣어줍니다.
		// input.txt파일에서 읽습니다
		// Config.ini / USE_INPUT_FILE = true, INPUT_FILE = input.txt
		Debug.timer.start(); // 코드의 실행시간 측정을 시작합니다.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int start = Integer.parseInt(st.nextToken());

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				testArray[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		Debug.print.arr(testArray, N, N, "testArray 입력 확인"); // Debug.print.arr(배열, 출력할 행크기, 출력할 열 크기, 배열출력전 메세지)
		Debug.print.arr(testArray, "testArray 전체 출력"); // 위 함수에서 출력할 행, 열크기를 생략하면 전체를 출력할 수 있습니다!
		Debug.print.arr(testArray); // 출력 메세지를 없앨수도 있습니다! 출력할 행 열크기가 없으므로 마찬가지로 전체를 출력합니다.
		Debug.breakPoint(); // 아무기능도없는 함수지만 브레이크포인트를 잡아서 멈출수있습니다!

		ans.append("dfs 경로 확인\n").append(start).append(' ');
		Debug.breakPoint();
		dfs(start, N);
		ans.append('\n');

		Debug.timer.chk(); // Debug.timer.start부터 이 라인까지 실행시간을 측정합니다.

		ans.append("최단 거리 확인\n");
		Debug.breakPoint();
		int[] dist = dijkstra(start, N);

		for (int i = 0; i < N; i++) {
			if (dist[i] == Integer.MAX_VALUE)
				ans.append("INF");
			else
				ans.append(dist[i]);
			ans.append(' ');
		}
		ans.append('\n');

		System.out.println(ans);

		Debug.timer.chk(); // Debug.timer.start부터 이 라인까지 실행시간을 측정합니다.
		
		Debug.timer.start(); // 코드의 실행시간 측정을 시작합니다.
		int test = 0;
		for (int i = 0; i < 500000000; i++) {
			test += i%15;
		}
		System.out.println(test);
		Debug.timer.chk(); // Debug.timer.start부터 이 라인까지 실행시간을 측정합니다.
	}

}
