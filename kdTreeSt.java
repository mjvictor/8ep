import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;




public class kdTreeST<Value> {
	private Node root;
	private int n;
	public static final boolean X = true;
	public static final boolean Y = false;

	private class Node{
		Point2D coor;
		Value val;
		Node right;
		Node left;
		boolean axis;
		RectHV rect;

		public Node(Point2D p, Value val, boolean axis) {
			coor = p;
			this.val = val;
			right = null;
			left = null;
			this.axis = axis;
			rect = null;
		}
	} 

	// construct an empty symbol table of points 
	public PointST(){
		root = null;
		n = 0;
	}
	// is the symbol table empty? 
	public boolean isEmpty(){
		return (root == null);
	}
	// number of points 
	public int size() {
	 	return n;
	}
	// associate the value val with point p
	public void put(Point2D p, Value val) {
		if (p == null || val == null) throw new java.lang.NullPointerException("Argument to put() is null");
		if (root == null) {
			Node new_node = new Node(p, val, X);
			new_node.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
		}
		else put(root, p, val);
	}
	private void put(Node node, Point2D p, Value val) {
		//X
		if (node.dir == X) {
			//maior ou igual(com y diferentes)
			if (p.x() > node.coor.x() || (p.x() == node.coor.x() && p.y() != node.coor.y())) {
				if (node.right == null) {
					Node new_node = new Node(p. val, !node.axis);
					//copia o ymin, xmax e ymax do retangulo do node, xmin passa a ser x do node
					new_node.rect = new RectHV(node.coor.x(), node.rect.ymin(), node.rect.xmax(), node.rect,ymax());

					node.right = new_node;
				}
				else {
					put(node.right, p, val);
				}
			}
			//menor
			else if (p.x() < node.coor.x()) {
				if (node.left == null) {
					Node new_node = new Node (p, val, !node.axis);
					//copia xmin, ymin e ymax do retangulo do node, xmax passa a ser o x do node
					new_node.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.coor.x(), node.rect.ymax());
					node.left = new_node;
				}
				else {
					put(node.left, p, val);
				}
			}
			//igual(com y iguais)
			else {
				node.val = val;
			}
		}

		//Y
		else {
			//maior
			if (p.y() > node.coor.y() || (p.y() == node.coor.y() && p.x() != node.coor.x())) {
				if (node.right == null) {
					Node new_node = new Node(p. val, !node.axis);
					//copia xmin, xmax e ymax do retangulo do node, ymin passa a ser o y do node.
					new_node.rect = new RectHV(node.rect.x(), node.coor.y(), node.rect.xmax(), node.rect,ymax());
					node.right = new_node;
				}
				else {
					put(node.right, p, val);
				}
			}
			//menor
			else if (p.y() < node.coor.y()) {
				if (node.left == null) {
					Node new_node = new Node (p, val, !node.axis);
					//copia xmin, ymin e xmax do retangulo do node, ymax passa a ser o y do node
					new_node.rect = new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.coor.y());
					node.left = new_node;
				}
				else {
					put(node.left, p, val);
				}
			}
			//igual
			else {
				node.val = val;
			}
		}

	}

	// value associated with point p 
	public Value get(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to get() is null");
		Value val = get (root, p);
		return val;
	}
	private Value get(Node node, Point2D p) {
		if (node == null) return null;
		//X
		if (node.dir == X){
			if (p.x() > node.coor.x() || (p.x() == node.coor.x() && p.y() != node.coor.y())) {
				return get(node.right, p);
			}
			else if (p.x() < node.coor.x()) {
				return get(node.left, p);
			}
			else {
				return node.val;
			}
		}
		//Y
		else {
			if (p.y() > node.coor.y() || (p.y() == node.coor.y() && p.x() != node.coor.x())) {
				return get(node.right, p);
			}
			else if (p.y() < node.coor.y()) {
				return get(node.left, p);
			}
			else {
				return node.val;
			}
		}
	}

	// does the symbol table contain point p? 
	public boolean contains(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to contains() is null");
		Value val = get(p);
		if (val == null) return false;
		return true;
	}
	// all points in the symbol table 
	public Iterable<Point2D> points() {
		Queue<Point2D> queue = new Queue<Point2D>();
		
		return queue;
	}
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {

	}
	// a nearest neighbor to point p; null if the symbol table is empty
	public Point2D nearest(Point2D p) {

	}

	// unit testing (required)
	public static void main(String[] args) {

	}
}