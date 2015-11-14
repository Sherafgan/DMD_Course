package BPT;

import java.io.*;

public class BPlus {

	public static void main (String[] args) {

		//Checking that we received exactly four arguments from the user
		if(args.length == 4)
		{
			String tInputFileName = args[0];//Acquiring input file name
			int t = Integer.parseInt(args[1]); // represents the maximum size of the nodes in the tree
			int oNum = Integer.parseInt(args[2]); // the number that should be Ordered
			record oRecord = new record();
			//Calling to readFromFile function, which will read the content of the file named fileName and concatenate it to one string, and returns it to the user
			String tOutputFileName = args[3];//Acquiring output file name
			//String tFileContent = readFromFile(tInputFileName);
			String inputFile = readFromFile(tInputFileName).trim(); // deletes spaces
			String[] numbers = inputFile.split(" "); // array of strings possesing the numbers
			record[] records = new record[numbers.length]; // array of records by the input numbers
			Tree T = new Tree(t,records.length);
			for(int i=0; i<numbers.length; i++){ // initialize records and inserts them to the tree
				records[i] = new record(Integer.parseInt(numbers[i])); 
				if(oNum==records[i].getNum())
					oRecord = records[i]; // the 'Order' record is found and saved
				T.Insert(records[i]); // insertion to the tree
			}
			String Content = ""+T.toString()+"\n"+T.Min_gap()+"\n"+T.Order(oRecord);
			System.out.println(T.toString());
			System.out.println(T.Min_gap());
			System.out.println(T.Order(oRecord));
			
			
			
			
			writeToFile(tOutputFileName, Content);//Calling to writeToFile function which will write the content string to the output file
			
		}
		else
		{
			System.out.println("No args were given , or more than four args");
		}
	}
	
	public static String readFromFile(String fileName)
	{
		
		String tContent = "";
		//Must wrap working with files with try/catch
		try{
			//Creating a file object
			File tFile = new File(fileName);
			//Init inputstream
			FileInputStream fstream = new FileInputStream(tFile);
			DataInputStream in = new DataInputStream(fstream);
			//Creating a buffered reader.
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			//Read File Line By Line
			while ((strLine = br.readLine()) != null){
				
				tContent = tContent + strLine + " ";//concatenating the line to content string
			}
			
			//Close the input stream
			in.close();
		}
		catch(Exception e)//Catch exception if any
		{
			System.err.println("Error: " + e.getMessage());
		}
		return tContent;
	}
	
	public static void writeToFile(String fileName, String Content){
		//Must wrap working with files with try/catch
				try {
					//Creating a file object
					File tFile = new File(fileName);
					// if file doesn't exists, then create it
					if (!tFile.exists()) {
						tFile.createNewFile();
					}
					//Init fileWriter
					FileWriter fw = new FileWriter(tFile.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(Content);
					//Close the output stream
					bw.close();
				} catch (IOException e) {//Catch exception if any
					
					System.err.println("Error: " + e.getMessage());
				}
	}
}
