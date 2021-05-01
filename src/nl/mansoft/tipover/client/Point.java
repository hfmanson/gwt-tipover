/*
 * Point.java
 *
 * Created on 19 juli 2005, 23:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package nl.mansoft.tipover.client;

/**
 *
 * @author Manson
 */
public class Point {
    int x;
    int y;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public boolean equals(Object o) {
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }    
}
