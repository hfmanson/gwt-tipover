package nl.mansoft.tipover.client;

public class Rectangle {
    /**
     * The X coordinate of the upper-left corner of the <code>Rectangle</code>.
     *
     * @serial
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @since 1.0
     */
    public int x;

    /**
     * The Y coordinate of the upper-left corner of the <code>Rectangle</code>.
     *
     * @serial
     * @see #setLocation(int, int)
     * @see #getLocation()
     * @since 1.0
     */
    public int y;

    /**
     * The width of the <code>Rectangle</code>.
     * @serial
     * @see #setSize(int, int)
     * @see #getSize()
     * @since 1.0
     */
    public int width;

    /**
     * The height of the <code>Rectangle</code>.
     *
     * @serial
     * @see #setSize(int, int)
     * @see #getSize()
     * @since 1.0
     */
    public int height;
    
    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is
     * specified as
     * {@code (x,y)} and whose width and height
     * are specified by the arguments of the same name.
     * @param     x the specified X coordinate
     * @param     y the specified Y coordinate
     * @param     width    the width of the <code>Rectangle</code>
     * @param     height   the height of the <code>Rectangle</code>
     * @since 1.0
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    } 
    /**
     * Checks whether or not this <code>Rectangle</code> contains the
     * point at the specified location {@code (x,y)}.
     *
     * @param  x the specified X coordinate
     * @param  y the specified Y coordinate
     * @return    <code>true</code> if the point
     *            {@code (x,y)} is inside this
     *            <code>Rectangle</code>;
     *            <code>false</code> otherwise.
     * @since     1.1
     */
    public boolean contains(int x, int y) {
        return inside(x, y);
    }
    /**
     * Checks whether or not this <code>Rectangle</code> contains the
     * point at the specified location {@code (X,Y)}.
     *
     * @param  X the specified X coordinate
     * @param  Y the specified Y coordinate
     * @return    <code>true</code> if the point
     *            {@code (X,Y)} is inside this
     *            <code>Rectangle</code>;
     *            <code>false</code> otherwise.
     * @deprecated As of JDK version 1.1,
     * replaced by <code>contains(int, int)</code>.
     */
    @Deprecated
    public boolean inside(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int x = this.x;
        int y = this.y;
        if (X < x || Y < y) {
            return false;
        }
        w += x;
        h += y;
        //    overflow || intersect
        return ((w < x || w > X) &&
                (h < y || h > Y));
    }
 }
