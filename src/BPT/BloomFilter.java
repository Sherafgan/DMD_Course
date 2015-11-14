package BPT;

public class BloomFilter {
	
	/**
	 * Bloom Filter probability Rate:
	 * n = records input
	 * m = boolean array length
	 * k = number of hash functions
	 * 
	 * FORMULA (1-e^(-mn/k))^m
	 * in our case:
	 * m = 4*n
	 * k = 4
	 * so formula is:
	 * (1-e^(-(4n^2)/4))^4n = (1-e^(-n^2))^4n
	 */
	
	/******************Fields******************/
	
	private boolean[] flags; // array of booleans that represents the flags
	private int a; // random between 1 and 100, used for the hash functions
	private int b; // random between 1 and 100, used for the hash functions
	
	/******************Constructors******************/
	
	public BloomFilter(int n){
		flags = new boolean[4*n]; // initialize the boolean array by the size of 4n
		for(int i=0; i<flags.length; i++){
			flags[i]=false; // initializing the fields with false value
		}
		a = (int) (Math.random()*100); // 1-100
		b = (int) (Math.random()*100);// 1-100
	}
	
	/****************Methods*******************/
	
	private int[] functions(int x){ // The hash functions
		int[] ans = new int[4];
		ans[0] = ((a*x+b)%29)%flags.length;
		ans[1] = ((a*x+b)%311)%flags.length;
		ans[2] = ((a*x+b)%571)%flags.length;
		ans[3] = ((a*x+b)%739)%flags.length;
		return ans; // returns 4 index that were implied by the hash functions
	}
	
	public void add(int x){ // adds a value to the bloom filter
		int[] index = functions(x);// get the indexes by send the value to the hash functions
		for(int i=0; i<index.length; i++)
			flags[index[i]] = true; // change the relevant flags to be true
	}
	
	/**
	 * recieves a value and check if that value does exists in the data structure with the probability of
	 * (1-e^(-n^2))^4n
	 * by checking the relevant flags
	 * @param x
	 * @return
	 */
	
	public boolean contains(int x){
		boolean ans=true;
		int[] index = functions(x); // get the indexes by send the value to the hash functions
		for(int i=0; i<index.length; i++)
			ans = ans&&flags[index[i]];
		return ans; //false if doesn't exists, true if might be exists (very high probability)
	}
	
}
