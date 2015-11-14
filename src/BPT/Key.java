package BPT;

public class Key extends record {
	/****************Fields*******************/
	private Node right; // represents the right son of the key
	private Node left; // represents the left son of the key
	
	/****************Constructors*******************/
	
	public Key(){
		super();
		right=null;
		left=null;
	}
	
	public Key (int num){
		super(num);
		right=null;
		left=null;
	}
	
	/****************Setters*******************/
	
	public void setLeft(Node x){ // sets the left son of the key
		left = x;
	}
	
	public void setRight (Node x){ // sets the right son of the key
		right = x;
	}
	
	/****************Getters*******************/
	
	public Node getLeft(){ // returns the left son of the key
		return left;
	}
	
	public Node getRight(){ // returns the right son of the key
		return right;
	}
	
}
