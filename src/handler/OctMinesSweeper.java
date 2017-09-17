package handler;

import java.awt.Point;
import java.util.ArrayList;

public class OctMinesSweeper extends MinesSweeper {
	public ArrayList<Point> GetSurroundCells(int x, int y) {
		ArrayList p = new ArrayList();
		if (this.InRange(x - 1, y)) {
			p.add(new Point(x - 1, y));
		}

		if (this.InRange(x + 1, y)) {
			p.add(new Point(x + 1, y));
		}

		if (this.InRange(x, y - 1)) {
			p.add(new Point(x, y - 1));
		}

		if (this.InRange(x, y + 1)) {
			p.add(new Point(x, y + 1));
		}

		if (this.InRange(x - 1, y - 1)) {
			p.add(new Point(x - 1, y - 1));
		}

		if (this.InRange(x + 1, y - 1)) {
			p.add(new Point(x + 1, y - 1));
		}

		if (this.InRange(x - 1, y + 1)) {
			p.add(new Point(x - 1, y + 1));
		}

		if (this.InRange(x + 1, y + 1)) {
			p.add(new Point(x + 1, y + 1));
		}

		return p;
	}
}