import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MaxPQ;
import java.util.Comparator;


public class KdTreeST<Value> {
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
		double dist;

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
	public KdTreeST(){
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
			root = new_node;
		}
		else put(root, p, val);
	}
	private void put(Node node, Point2D p, Value val) {
		//X
		if (node.axis == X) {
			//maior ou igual(com y diferentes)
			if (p.x() > node.coor.x() || (p.x() == node.coor.x() && p.y() != node.coor.y())) {
				if (node.right == null) {
					Node new_node = new Node(p, val, !node.axis);
					//copia o ymin, xmax e ymax do retangulo do node, xmin passa a ser x do node
					new_node.rect = new RectHV(node.coor.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());

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
					Node new_node = new Node(p, val, !node.axis);
					//copia xmin, xmax e ymax do retangulo do node, ymin passa a ser o y do node.
					new_node.rect = new RectHV(node.rect.xmin(), node.coor.y(), node.rect.xmax(), node.rect.ymax());
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
		if (node.axis == X){
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
		Node aux;
		Queue<Node> node = new Queue<Node>();
		Queue<Point2D> queue = new Queue<Point2D>();
		
		if (root != null) {
			node.enqueue(root);
			//desenfileira um nó da node, enfileira na queue e enfileira os filhos
			//na node na ordem da esquerda para direita.  
			while (!node.isEmpty()) {
				aux = node.dequeue();
				queue.enqueue(aux.coor);
				if (aux.left != null) node.enqueue(aux.left);
				if (aux.right != null) node.enqueue(aux.right);
			}
		}

		return queue;
	}
	// all points that are inside the rectangle 
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) throw new java.lang.NullPointerException("Argument to range() is null");
		Queue<Point2D> queue = new Queue<>();
		if (root != null) range(root, rect, queue);
		return queue;
	}
	private void range(Node node, RectHV rect, Queue<Point2D> queue) {
		if (rect.contains(node.coor)) {
			queue.enqueue(node.coor);
		}
		if (node.right != null && rect.intersects(node.right.rect)) {
			range(node.right, rect, queue);
		}
		if (node.left != null && rect.intersects(node.left.rect)) {
			range(node.left, rect, queue);
		}
	}
	// a nearest neighbor to point p; null if the symbol table is empty
	public Point2D nearest(Point2D p) {
		if (p == null) throw new java.lang.NullPointerException("Argument to nearest() is null");
		if (root == null) return null;
		Node max = new Node (root.coor, root.val, root.axis);
		nearest (root, p, max);
		return max.coor;
	}

	//1.verificar se a distancia atual do ponto é menor do que o campeao
	//2.verificar qual é a melhor subarvore pra ir (o mesmo lado do splitting)
	//3.se a distancia do ponto ate o retangulo da subarvore for maior que a distancia do ponto campeao ao ponto p, nem olha a subarvore.

	private void nearest(Node node, Point2D p, Node max) {
		//verifica se a distancia do node é maior que a distancia
		double maxDist = max.coor.distanceSquaredTo(p);
		if (node.coor.distanceSquaredTo(p) < maxDist) {
			max.coor = node.coor;
		}

		if (node.right != null && node.left != null) {
			//olha a direita primeiro e depois olha a esquerda 
			if (node.right.rect.distanceSquaredTo(p) < node.left.rect.distanceSquaredTo(p)) {
				if (node.right.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.right, p, max);
				if (node.left.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.left, p, max);
			}
			//olha a esquerda primeiro e depois olha a direita
			else {
				if (node.left.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.left, p, max);
				if (node.right.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.right, p, max);	
			}
		}
		else if (node.right == null && node.left != null) {
			//vai pra left se precisar
			if (node.left.rect.distanceSquaredTo(p) < maxDist)
				nearest(node.left, p, max); 
		}
		else if (node.left == null && node.right != null) {
			//vai pra right se precisar.
			if (node.right.rect.distanceSquaredTo(p) < maxDist)
				nearest(node.right, p, max);	
		}
	} 

	public Iterable<Point2D> nearest(Point2D p, int k) {
		if (p == null || k <= 0) throw new java.lang.NullPointerException("Argument to nearest() is null");
		//inicializar direitinho
		MaxPQ<Node> knear = new MaxPQ<>(new nearestComparator());
		Queue<Point2D> queue = new Queue<>();
		if (root == null) return queue;
		nearest(root, p, k, knear);
		for (Node tmp : knear) {
			queue.enqueue(tmp.coor);
		}
		return queue;
	}

	private void nearest (Node node, Point2D p, int k, MaxPQ<Node> max) {
		if (max.size() < k) {
			node.dist = node.coor.distanceSquaredTo(p);
			max.insert(node);
			if (node.right != null) nearest(node.right, p, k, max);
			if (node.left != null) nearest(node.left, p, k, max);
		}
		else {
			double maxDist = max.max().coor.distanceSquaredTo(p);
			if (node.coor.distanceSquaredTo(p) < maxDist) {
				max.delMax();
				max.insert(node);
			}
			if (node.right != null && node.left != null) {
				if (node.right.rect.distanceSquaredTo(p) < node.left.rect.distanceSquaredTo(p)) {
					if (node.right.rect.distanceSquaredTo(p) < maxDist)
						nearest(node.right, p, k,max);
					if (node.left.rect.distanceSquaredTo(p) < maxDist)
						nearest(node.left, p, k, max);
				}
				//olha a esquerda primeiro e depois olha a direita
				else {
					if (node.left.rect.distanceSquaredTo(p) < maxDist)
						nearest(node.left, p, k, max);
					if (node.right.rect.distanceSquaredTo(p) < maxDist)
						nearest(node.right, p, k, max);	
				}	
			}
			else if (node.right == null && node.left != null) {
				//vai pra left se precisar
				if (node.left.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.left, p, k, max); 
			}
			else if (node.left == null && node.right != null) {
				//vai pra right se precisar.
				if (node.right.rect.distanceSquaredTo(p) < maxDist)
					nearest(node.right, p, k, max);	
			}

		}
	}

	private class nearestComparator implements Comparator<Node> {
		public int compare (Node node1, Node node2) {
			if (node1.dist > node2.dist) return 1;
			else if (node1.dist < node2.dist) return -1;
			return 0;
		}
	}
	// unit testing (required)
	public static void main(String[] args) {

	}
}