package BPT;

public class record {
	/****************Fields*******************/
	private int num; // represents the value of the record
	
	/****************Constructors*******************/
	public record(){
		num=0;
	}
	
	public record(int num){
		this.num=num;
	}
	
	/****************Getters*******************/
	
	public int getNum(){ // returns the value of the record
		return this.num;
	}
	
	/****************Methods*******************/
	
	public String toString(){
		String ans = ""+num;
		return ans;
	}
}
