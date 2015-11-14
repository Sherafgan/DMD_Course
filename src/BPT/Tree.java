package BPT;

public class Tree {
	/****************Fields*******************/
	private Node Root; // represents the root of the tree
	private Node First; // represents the first leaf
	private int MinGap; // represent the minimum gap in the tree
	private BloomFilter BF; // represents the Bloom Filter of the tree

	/****************Constructors*******************/

	public Tree(){
		Init(0);
	}

	public Tree(int t,int BFsize){
		Init(t);
		this.BF = new BloomFilter(BFsize); // initializing the Bloom Filter
	}

	/****************Getters Setters*******************/

	public void setFirst(Node n){ // sets a first
		this.First = n;
	}

	public Node getFirst(){ // returns the first leaf
		return First;
	}

	public void setMinGap(int m){ // sets the minimum gap
		this.MinGap=m;
	}

	public int Min_gap(){ // returns the minimum gap, O(1)
		return MinGap;
	}

	public void addToBF(record x){ // adds the record to the bloom filter
		this.BF.add(x.getNum());
	}

	private boolean contains(record x){ // tells if the record is inside the tree, by using bloom filter
		return BF.contains(x.getNum());
	}

	/****************Methods*******************/

	public void Init(int t){
		Root = new Node(t); // creates the root
		First = Root; // at first, the root is also the first leaf
		MinGap=Integer.MAX_VALUE; // initializing the min gap
	}

	/**
	 * recieving a record and insert it to it's rightful leaf
	 * makes splits if needed
	 * 
	 * O(tlog_t n)
	 * 
	 * @param x
	 */

	public void Insert(record x){
		addToBF(x); // adds the record to the Bloom Filter as well
		Node curr = Root;
		int i=-1;
		while(!curr.isLeaf()){ // climbing the tree until reaching a leaf
			i++;
			if(i>=curr.size()){ // we made it to the end of the node
				curr = ((Key) curr.getLast()).getRight();
				i=-1;
			}
			else if(x.getNum()<=curr.get(i).getNum()){ // we found the right Key
				curr = ((Key) curr.get(i)).getLeft();
				i=-1;
			}
		}
		curr.Insert(x); // insert x into the leaf, makes splits if needed

		////////adjust minGap if needed//////////////
		if(curr.getMin()<MinGap)
			setMinGap(curr.getMin()); 
		/////////////////////////////////////////////

		if(Root.getFather()!=null) // adjusting the root if needed
			Root = Root.getFather();
	}
	
	/**
	 * the Search method is using the Bloom Filter to know for sure if the record doesn't exists in the tree.
	 * when the record doesn't in the tree - immediately returns false by using the bloom filter
	 * 
	 * it's O(1) with the probability of (1-e^(-n^2))^4n which is very high.
	 * 
	 * if the bloom filter says the record is inside the tree, we climb all the way from the root to the leaf
	 * the record should be inside, and we search that exact leaf and return true/false for sure.
	 * 
	 * it's O(tlog_t n) because we search all over the height of the tree.
	 * 
	 * recieves a record and returns true if the tree contains the record and false if not.
	 * @param x
	 * @return
	 */
	
	public boolean search(record x){
		if(!contains(x)) // checks the bloom filter for that record.
			return false;
		// if it's true, we need to ensure it
		else{ // ensuring that the tree DOES contain x.
			Node curr = Root;
			int i=-1;
			while(!curr.isLeaf()){ // climbing the tree until reaching a leaf
				i++;
				if(i>=curr.size()){ // we made it to the end of the node
					curr = ((Key) curr.getLast()).getRight();// climb higher on the tree
					i=-1;
				}
				else if(x.getNum()<=curr.get(i).getNum()){ // we found the right Key
					curr = ((Key) curr.get(i)).getLeft(); // climb higher on the tree
					i=-1;
				}
			}
			return curr.search(x);	// true if the node contains x, false if not.
		}
	}

	/**
	 * recieving a record and returns it's place by number
	 * 
	 * @param x
	 * @return
	 */

	public int Order(record x){
		int counter = 0; // counter is also our result
		int i=0;
		Node curr = Root;
		while(!curr.isLeaf()){ // climbs the tree until reaching one of the leaves
			if((i!=curr.size())&&(x.getNum()>curr.get(i).getNum())){
				counter+=((Key) curr.get(i)).getLeft().getWeight();
				// adds the whole weight of the left son
			}
			else if(i==curr.size()){ // we made it to the end of the node
				curr = ((Key)curr.getLast()).getRight();
				i=-1;
			}
			else{// found the right key
				curr = ((Key)curr.get(i)).getLeft();
				i=-1;
			}
			i++;
		}
		i=0;
		while((i!=curr.size())&&(x.getNum()>=curr.get(i).getNum())){ 
			// we are inside the right leaf, count the records until reaching x
			counter++;
			i++;
		}

		return counter;
	}

	public String toString(){ // the String is the leaves link list by the rules
		String ans="";
		Node curr = First;
		while(curr!=null){
			ans=ans+"#"+curr.toString();
			curr=curr.getNext();
		}
		ans = ans.substring(1);
		return ans;
	}
}
