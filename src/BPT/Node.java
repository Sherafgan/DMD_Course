package BPT;

import java.util.Vector;

public class Node {
	/****************Fields*******************/
	private boolean isLeaf; // a boolean variable that says if the node is leaf or not
	private Vector<record> innerList; // represents the inner keys/records of the node
	private Node next; // when the node is a leaf, it represents the next leaf
	private Node prev;// when the node is a leaf, it represents the previous leaf
	private Node father; // represents the father of the node
	private int size; // represents the maximum size of the node
	private int sizeCounter; // represents how many keys/records are inside the node
	private int minGap=Integer.MAX_VALUE; // represent the minimum gap in the node
	private int weight; // represent how many records are under this node responsibility

	/****************Constructors*******************/
	
	public Node(int t){ // initialize the Node with the maximum size of t
		isLeaf=true;
		this.innerList=new Vector<record>();
		next = null;
		size=t;
		sizeCounter=0;
		weight=0;
		father=null;
		prev=null;
	}

	public Node(boolean leaf, int t){ // initialize the Node with considering if it's a leaf or not
		isLeaf=leaf;
		if(isLeaf){
			this.innerList=new Vector<record>(); // when the node is a leaf, the vector posses records
			next=null;
		}
		else{
			this.innerList=new Vector<record>();// when the node is not a leaf, the vector posses keys
		}
		this.size = t;
		sizeCounter=0;
		father=null;
		prev=null;
		weight=0;
	}

	/****************Getters Setters*******************/

	public void setNext(Node n){ // sets the next leaf
		this.next=n;
	}

	public void setFather(Node n){// sets the father of the node
		this.father = n;
	}

	public void setPrev(Node n){// sets the previous leaf
		this.prev=n;
	}

	public Node getNext(){ // returns the next leaf
		return next;
	}

	public Node getFather(){ // returns the father
		return father;
	}

	public Node getPrev(){ // returns the previous leaf
		return prev;
	}

	public int getMin(){ // returns the minimum gap in the node
		return minGap;
	}

	public int size(){ // returns the size of the node
		return innerList.size();
	}

	public record get(int i){ // returns the indexed record/key
		return innerList.get(i);
	}

	public record getFirst(){ // returns the first record/key
		return innerList.firstElement();
	}

	public record getLast(){ // returns the last record/key
		return innerList.lastElement();
	}

	public void setWeight(int w){ // sets the weight of the node
		this.weight = w;
	}

	public void weightPlus(){ // increase the weight
		this.weight++;
	}

	public void weightMinus(){ // decrease the weight
		this.weight--;
	}

	public int getWeight(){ // returns the weight of the node
		return this.weight;
	}

	/****************Methods*******************/
	
	public boolean isEmpty(){ // tells if the node is empty
		return innerList.isEmpty();
	}
	public boolean isFull(){ // tells if the node is full
		return sizeCounter==size;
	}

	public boolean isLeaf(){ // returns if the node is a leaf or not
		return isLeaf;
	}

	public boolean search(record x){ // returns true if the requested record is inside the node
		return innerList.contains(x);
	} // O(t) = O(1)
	
	
	/**
	 * recieves a record and inserts it to the node, makes splits if needed
	 * 
	 * O(tlog_t n) --- because splits might go all the way to the root
	 * 
	 * @param x
	 */
	
	public void Insert(record x){ // the vector must not be full, but might be full after that.

		if(isEmpty()){ // the node is empty, just inserts the record
			innerList.insertElementAt(x, 0);
			sizeCounter++; // adjust size
		}
		else{
			boolean inserted = false;
			int i=-1;
			while(!inserted){ // continue until the record has been inserted
				i++;
				if (i>=sizeCounter){ // we made it to the end of the vector
					innerList.insertElementAt(x, i);
					sizeCounter++; // adjust size
					inserted = true; // insertion has happened
				}
				else if((get(i)!=null)&&(x.getNum()<get(i).getNum())){// we found the right place
					innerList.insertElementAt(x, i);
					if(!isLeaf()){
						((Key) get(i+1)).setLeft(((Key) get(i)).getRight());
					}
					inserted = true; // insertion has happened
					sizeCounter++; // adjust size
				}
			}
			
			///////adjust minimum gap if needed///////
			if(isLeaf){
				int leftMin=Integer.MAX_VALUE;
				int rightMin=Integer.MAX_VALUE;
				if(getFirst()!=x){
					leftMin=Math.abs(get(i-1).getNum()-x.getNum());
				}

				else if(prev!=null){
					leftMin=Math.abs(x.getNum()- prev.getLast().getNum());
				}

				if(getLast()!=x){
					rightMin=Math.abs(get(i+1).getNum()-x.getNum());
				}

				else if(next!=null){
					rightMin=Math.abs(x.getNum()- next.getFirst().getNum());
				}
				minGap=Math.min(minGap,Math.min(leftMin, rightMin));
			}
			////////////////////////////////////


		}
		///Adjust Weight////////
		if(isLeaf){
			this.weightPlus();
			Node curr = getFather();
			while (curr!=null){
				curr.weightPlus();
				curr = curr.father;
			}
			////////////////////
		}
		if (isFull()) Split(); // the Node is now full, split has to happen!
	}

	public record remove(int i){ // removes a specific key/record
		sizeCounter--; // adjust size
		return innerList.remove(i);
	}


	/**
	 * the Split method is the most important node in a b tree, and the most difficult one.
	 * here the split method is recursive (only if the father is full).
	 * 
	 * when t is odd - most records stays in the old node
	 * when t is dual - half records stays and half records go to the new node
	 * 
	 * O(log_t n) - because splits might go all the way down to the root
	 * 
	 */
	
	private void Split(){ // only happens on a full leaf!
		Node b = new Node(isLeaf,size); // brother
		int i; // index

		if (size()%2==0) // the size is a dual number, cuts in half
			i = size()/2;
		else // the size is an odd number, most records stays at the old node
			i = size()/2 +1;

		while (i!=size()){ // let the size come to i
			if(!isLeaf)
					setWeight(getWeight()-((Key) get(i)).getRight().getWeight()); // adjust weight for a key
			else
				this.weightMinus(); // adjust weight for a record
			b.Insert(this.remove(i)); // removes from the old node and inserts to the new node
		}
		// after this loop, 'a' is cut to half and 'b' got the other half
		Key k = new Key(innerList.lastElement().getNum()); // creates new key after his successor;
		k.setLeft(this); // sets the left son of the key
		k.setRight(b); // sets the right son of the key
		int w=0; // helping variable to adjust the weight of b
		i=0;
		// now it's time to take care of b
		if(!b.isLeaf()){
			while(i!=b.size()){
				((Key)b.get(i)).getLeft().setFather(b); // tells the sons they have a new father now
				w+=((Key)b.get(i)).getLeft().getWeight(); // counts the weight of the sons
				i++;
			}
			((Key)b.getLast()).getRight().setFather(b); // also tell the most right son he got a new father
			w+=((Key)b.getLast()).getRight().getWeight();// counts his weight as well
			b.setWeight(w); // sets the weight of b
		}

		if(isLeaf){ // if b is a leaf adjust his next and previous leaves
			b.setNext(this.getNext());
			setNext(b);
			b.setPrev(this);
		}
		else{ // the node is internal, we need to remove the key
			setWeight(getWeight()-((Key)getLast()).getRight().getWeight()); // adjust the weight
			innerList.remove(innerList.lastElement()); // removes the last key
			sizeCounter--; // adjust size
		}

		if (father==null){ // there is no father for this one! probably the root
			Node f = new Node(false,size); // creates a new node to be the father
			f.Insert(k); // insert the key to the new father, makes splits if needed
			setFather(f); // set the father of the node
			b.setFather(f);// same father for the brother
			father.setWeight(this.getWeight()+b.getWeight()); // set the weight of the father
		}
		else{ // the is already a father for 
			b.setFather(father); 
			father.Insert(k); // recursive if father is now full
		}


	}
	
	public String toString(){ // prints the inner records by the rules
		String ans = "";

		for(int i=0; i<innerList.size(); i++){
			if(get(i)!=null)
				ans = ans+","+get(i).toString();
		}
		ans=ans.substring(1);

		return ans;
	}
}
