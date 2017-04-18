//Kaushil Ruparelia CS610 9169 prp


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kaushilruparelia
 *
 */
public class hits9169 {

	private static List<ArrayList<Integer>> adjacencyList;
	private static double[] authority;
	private static double[] hub;
	private static double[] old_authority;
	private static double[] old_hub;
	private static int nodeCount;
	private static int edgeCount;
	private static int iterations;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length < 3) {
			System.out.println("Please enter valid command line argument!!!");
			return;
		}
		
		//Read the arguments
		String filename = args[2];
		int initialValue = Integer.parseInt(args[1]);
		iterations =  Integer.parseInt(args[0]);
		
		//Get the input
		String input = "";
		try {
			input = readFile(filename);
		} catch (IOException e) {
			//  Auto-generated catch block
			System.out.println("Could not read the file!");
			e.printStackTrace();
			return;
		}
		
		//Split the input as rows of nodes
		String[] rows = input.split("\n");
		
		//Check the boundary condition
		if (rows.length < 2) {
			System.out.println("Invalid input");
			return;
		}

		try {
			//Get the header row and its details
			int[] headerArray = extractNodeValues(rows[0]);
			
			//Get the number of nodes and edges
			nodeCount = headerArray[0]; 
			edgeCount = headerArray[1];
			
			if (edgeCount < rows.length -1) {
				System.out.println("Inconsistent data!");
				return;
			}
			
			if (nodeCount > 10) {
				iterations = 0;
				initialValue = -1;
			}
			
			//Initialize Variables
			initializeVariables(initialValue);
			
			//Create adjacency List
			createAdjacencyList(rows);
			
			int count =0;
			printAuthAndHub(count);
			
			//Loop until the values converge
			do {
				//Calculate Authority
				calculateAuthority();
				
				//Calculate hub
				calculateHub();
				
				//Scale the calculation
				scale();
				
				//Print the values
				++count;
				if (nodeCount <= 10) {
					printAuthAndHub(count);
				}
				
			} while (!didConverge(count));
			
			if (nodeCount > 10) {
				printAuthAndHubForHigherNodes(count);
			}
			
		} catch (Exception e) {
			// Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		

	}
	
	/**
	 * Reads a file and  returns a Strings Array
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		 Path path = Paths.get(filePath);
		 List<String> inputFileList =  Files.readAllLines(path, StandardCharsets.US_ASCII);
		 String inputString = "";
		 for (String string : inputFileList) {
			 inputString = inputString.concat(string) + "\n";
		}
		 		 
		 return inputString;
	}
	
	
	/**
	 * Initializes the adjacencyList, hub and authority
	 * @param initialValue
	 */
	public static void initializeVariables(int initialValue) {
		// Initialize the values
		adjacencyList = new ArrayList<ArrayList<Integer>>(nodeCount);
		authority = new double[nodeCount];
		hub = new double[nodeCount];
		old_authority = new double[nodeCount];
		old_hub = new double[nodeCount];
		
		
		double init = 0;
		switch (initialValue) {
		case 0:
		case 1:
			init = initialValue;
			break;
		
		case -1:
			init = 1 / (double)nodeCount;
			break;
		
		case -2:
			init = 1 / Math.sqrt(nodeCount);
			break;
			
		default:
			init = initialValue;
			break;
		}
		
		for (int i = 0; i < nodeCount; i++) {
			adjacencyList.add(new ArrayList<>());
			hub[i] = init;
			authority[i] = init;
		}
	}
	
	/**
	 * Creates an adjacency list from the input.
	 * @param rows
	 * @throws Exception 
	 */
	public static void createAdjacencyList(String[] rows) throws Exception {
		//Create an adjacency list of the edges
		int[] row;
		int node1, node2;
		for (int index = 1; index <= edgeCount; index++) {
			
			row = extractNodeValues(rows[index]);
			node1 = row[0];
			node2 = row[1];
			
			if (node1 >= nodeCount || node2 >= nodeCount) {
				System.out.println("Invalid input in rows 2: "+node1+" "+node2+ " "+ adjacencyList.size());
				return;
			}
			adjacencyList.get(node1).add(node2);		
		}
	}
	
	/**
	 * Prints the adjacency list.
	 */
	public static void printAdjacencyList() {
		for (List<Integer> list : adjacencyList) {
			for (Integer integer : list) {
				System.out.print(integer + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Extracts the Node Values
	 * @param line
	 * @return An array of integers with 2 values.
	 * @throws Exception
	 */
	public static int[] extractNodeValues(String line) throws Exception {
		String []row = line.split(" ");
		
		if (row.length != 2) {
			throw new Exception("Invalid Input found in row:"+row);
		}
		
		int[] result = new int[2];
		result[0] = Integer.parseInt(row[0]);
		result[1] = Integer.parseInt(row[1]);
		
		return result;
	}
	
	
	/**
	 * Calculate the Authority values
	 */
	public static void calculateAuthority() {
		double output;
		for (int i = 0; i < authority.length; i++) {
			old_authority[i] = authority[i];
			output = 0;
			for (int j = 0; j < hub.length; j++) {
				if (adjacencyList.get(j).contains(i)) {
					output += hub[j];
				}
			}
			authority[i] = output;
		}
	}
	
	/**
	 * Calculate the Hub values
	 */
	public static void calculateHub() {
		
		double output;
		for (int i = 0; i < hub.length; i++) {
			old_hub[i] = hub[i];
			output = 0;
			for (int j = 0; j < authority.length; j++) {
				if (adjacencyList.get(i).contains(j)) {
					output += authority[j];
				}
			}
			hub[i] = output;
		}
	}
	
	
	/**
	 * Scale the calculated Values
	 */
	public static void scale() {
		
		scaleArray(authority);
		
		scaleArray(hub);
	}
	
	/**
	 * Scale an array by dividiing each value with a square-root of sum of squares of all values.
	 * @param array
	 */
	public static void scaleArray(double[] array) {
		double scalingFactor = 0;
		
		for (int i = 0; i < array.length; i++) {
			scalingFactor += Math.pow(array[i], 2);
		}
		scalingFactor = Math.sqrt(scalingFactor);
		for (int i = 0; i < array.length; i++) {
			array[i]/= scalingFactor;
		}
	}
	
	/**
	 * Check if the values of authority martix converged
	 * @return True if the conversion is successful
	 */
	public static boolean didConverge(int current_iteration) {
		double multiplicationFactor = 0;
		if (iterations > 0) {
			return current_iteration == iterations;
		}
		else {
			if (iterations == 0) {
				multiplicationFactor = 100000;
			}
			else  {
				multiplicationFactor = Math.pow(10, (iterations * -1));
			}

			for (int i = 0; i < authority.length; i++) {
				if ((int)Math.floor(authority[i]*multiplicationFactor) != (int)Math.floor(old_authority[i]*multiplicationFactor)) {
					return false;
				}
			}
			for (int i = 0; i < hub.length; i++) {
				if ((int)Math.floor(hub[i]*multiplicationFactor) != (int)Math.floor(old_hub[i]*multiplicationFactor)) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Print the Authority and Hub Values
	 */
	public static void printAuthAndHub(int iteration) {
		
		if (iteration == 0) {
			System.out.print("Base : "+ iteration + " : ");
		}
		else {
			System.out.print("Iterat : "+ iteration + " : ");
		}
		
		DecimalFormat numberFormat = new DecimalFormat("0.0000000");
		
		for (int i = 0; i < authority.length; i++) {
			 System.out.print("A/H["+ i + "] = "+numberFormat.format(Math.floor(authority[i] * 1e7)/1e7) + " / " + numberFormat.format(Math.floor(hub[i] * 1e7)/1e7) + " ");
		}
		System.out.println();
	}
	
	public static void printAuthAndHubForHigherNodes(int iteration) {
		
		System.out.println("Iterat : "+ iteration + " : ");
		DecimalFormat numberFormat = new DecimalFormat("0.0000000");
		
		for (int i = 0; i < authority.length; i++) {
			 System.out.println("A/H["+ i + "] = "+numberFormat.format(Math.floor(authority[i] * 1e7)/1e7) + " / " + numberFormat.format(Math.floor(hub[i] * 1e7)/1e7) + " ");
		}
		System.out.println();
	}
}
