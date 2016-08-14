import java.util.Vector;

public class BinaryTreeTest {

	public static void main(String[] args) {		
		
		//构造二叉树
		Node a = new Node(5);
		Node b = new Node(3);
		Node c = new Node(2);
		Node d = new Node(7);
		Node e = new Node(1);
		Node f = new Node(8);
		Node g = new Node(4);
		Node h = new Node(6);
		
		a.setLeft(b);
		a.setRight(c);
		
		b.setLeft(d);
		b.setRight(f);
		
		d.setRight(e);
		
		f.setLeft(g);
		f.setRight(h);
		
		Operation op = new Operation(a);
		op.preOrder();
		op.postOrder();
		op.inOrder();
		op.size();
		op.printAll();
	}
}

class Node implements Comparable<Node>{
	private int val = 0;
	private Node left = null, right = null;
	
	public Node() {}
	public Node(int val) {
		this.val = val;
	}
	public Node(int val, Node left, Node right) {
		this.val = val;
		this.left = left;
		this.right = right;
	}
	
	public int getVal() {return val;}
	public void setVal(int val) {this.val = val;}
	
	public Node getLeft() {return left;}
	public void setLeft(Node left) {this.left = left;}
	
	public Node getRight() {return right;}
	public void setRight(Node right) {this.right = right;}
	
	@Override
	public int compareTo(Node node) {
		if(this.getVal() < node.getVal()) return -1;
		else if(this.getVal() > node.getVal()) return 1;
		else return 0;
	}
	
	public boolean equals(Node node) {
		return this.getVal() == node.getVal();
	}
}

class Operation {
	
	private Node root = null;
	private Vector<Node> vec = null;
	
	public Operation(Node root) {
		this.root = root;
		vec = new Vector<Node>();
		addNodeToVector(root);
		vec.sort(null);;
	}
	
	//预处理
	private void addNodeToVector(Node root) {
		if(root == null) return;
		vec.add(root);
		addNodeToVector(root.getLeft());
		addNodeToVector(root.getRight());
	}
	
	//前序
	public void preOrder() {
		System.out.print("pre order: ");
		preOutput(root);
		System.out.println();
	}
	
	private void preOutput(Node node) {
		if(node == null) return;
		System.out.print(node.getVal() + " ");
		preOutput(node.getLeft());
		preOutput(node.getRight());
	}
	
	//后序
	public void postOrder() {
		System.out.print("post order: ");
		postOutput(root);
		System.out.println();
	}
	
	private void postOutput(Node node) {
		if(node == null) return;
		postOutput(node.getLeft());
		postOutput(node.getRight());
		System.out.print(node.getVal() + " ");
	}
	
	//中序
	public void inOrder() {
		System.out.print("in order: ");
		inOutput(root);
		System.out.println();
	}
	
	private void inOutput(Node node) {
		if(node == null) return;
		inOutput(node.getLeft());
		System.out.print(node.getVal() + " ");
		inOutput(node.getRight());
	}
	
	//统计数量
	public void size() {
		System.out.println("size = " + vec.size());
	}
	
	//按升序打印出所有值
	public void printAll(){
		System.out.println("All elements in ascending order:");
		for(Node node : vec) {
			System.out.print(node.getVal() + " ");
		}
		System.out.println();
	}
}
