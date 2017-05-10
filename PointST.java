import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Queue;

public class PointST<Value> {
	RedBlackBST<Point2D, Value> bst;

	// construct an empty symbol table of points 
	public PointST(){
		bst = new RedBlackBST<Point2D, Value>();
	}
	// is the symbol table empty? 
	public boolean isEmpty(){
		return (bst.size() == 0);
	}
	// number of points 
	 public int size() {
	 	return bst.size();
	}
	// associate the value val with point p
	public void put(Point2D p, Value val) {
		if (p == null || val == null) throw new java.lang.NullPointerException("Argument to put() is null");
		bst.put (p, val);
	}
	// value associated with point p 
	public Value get(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to get() is null");
		return bst.get(p);
	}
	// does the symbol table contain point p? 
	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to contains() is null");
		return bst.contains(p);
	}
	// all points in the symbol table 
	public Iterable<Point2D> points() {
		return bst.keys();
	}
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new java.lang.NullPointerException("Argument to range() is null");
		Iterable<Point2D> queue = points();
		Queue<Point2D> new_queue = new Queue<Point2D>();

		for (Point2D p : queue) {
			if (rect.contains(p)) {
				new_queue.enqueue(p);
			}
		}

		return new_queue;
	}
	// a nearest neighbor to point p; null if the symbol table is empty
	public Point2D nearest(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to nearst() is null");
		if (bst.size() == 0) return null;
		Point2D near = null;
		Iterable<Point2D> aux = points();
		for (Point2D tmp : aux) {
			if (near == null || p.distanceSquaredTo(tmp) < p.distanceSquaredTo(near)) {
				near = tmp;
			}
		}
		return near;
	}	
	// unit testing (required)
	public static void main(String[] args) {
		
	}
}