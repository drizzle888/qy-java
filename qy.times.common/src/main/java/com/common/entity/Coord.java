package com.common.entity;

public class Coord {
	public int x;
    public int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 重写方法
     */
    @Override
    public boolean equals(Object obj) { 
        if (obj == null) {
            return false;
        }

        if (obj instanceof Coord) {
            Coord c = (Coord) obj;

            return (x == c.x) && (y == c.y);
        }

        return false;
    }
    
    @Override
	public String toString() {
        return String.format("[x=%s y=%s]", this.x, this.y);
    }
}
