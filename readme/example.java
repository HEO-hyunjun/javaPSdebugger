package javaPSdebugger.readme;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

import javaPSdebugger.Debug;
import javaPSdebugger.util.CoordinateDebugger;

class Point implements CoordinateDebugger {
	int r, c;

	Point(Point p) {
		this.r = p.r;
		this.c = p.c;
	}

	Point(int r, int c) {
		this.r = r;
		this.c = c;
	}

	@Override
	public String toString() {
		return "Point [r=" + r + ", c=" + c + "]";
	}

	@Override
	public int getRow() {
		return r;
	}

	@Override
	public int getCol() {
		return c;
	}

}

public class example {
	static int N, M;
	static char[][] map;
	static int[][] visit;
	static int[] dr = { -1, 1, 0, 0 }, dc = { 0, 0, -1, 1 };

	static boolean isRange(Point p) {
		return 0 <= p.r && p.r < N && 0 <= p.c && p.c < M;
	}

	static int getMax() {
		int ret = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < M; j++)
				ret = ret < visit[i][j] ? visit[i][j] : ret;
		return ret;
	}

	static void bfs(Point start) {
		Queue<Point> q = new LinkedList<>();
		q.add(start);
		visit[start.r][start.c] = -1;
		int cnt = 1;
		while (!q.isEmpty()) {
			int size = q.size();
			for (int step = 0; step < size; step++) {
				Point now = q.poll();
				for (int i = 0; i < 4; i++) {
					Point next = new Point(now);
					next.r += dr[i];
					next.c += dc[i];
					if (isRange(next) && visit[next.r][next.c] == 0) {
						visit[next.r][next.c] = cnt;
						q.add(next);
					}
					Debug.print.arr(visit, new Point[] { now, next }, "now = " + now, "next = " + next);
					Debug.breakPoint();
				}
			}
			cnt++;
			Debug.print.arr(visit, "cnt = " + cnt);
		}
		return;
	}

	public static void main(String[] args) throws IOException {
		Debug.start(new example());
		Debug.timer.start();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new char[N][M];
		visit = new int[N][M];

		bfs(new Point(0, 0));
		Debug.print.arr(visit, "after bfs visit");
		System.out.println("ans = " + getMax());
		Debug.timer.chk();
	}
}
