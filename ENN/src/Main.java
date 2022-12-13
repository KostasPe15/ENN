import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	static int columns = 0;
	static long rows = 0;

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		String line = ""; 
		String[] data = null;
		String first = "";
		ArrayList<ArrayList<Float> > theList = new ArrayList<ArrayList<Float> >();
		ArrayList<String> classList = new ArrayList<>();
		boolean flag = true;
		
		System.out.print("Write the path of the csv file: ");
		String path = keyboard.nextLine();
		
		System.out.print("Write the k: ");
		int k = keyboard.nextInt();
		keyboard.close();
		
		//Read from csv
		try   {  
			BufferedReader br = new BufferedReader(new FileReader(path));  
			while ((line = br.readLine()) != null)  { 
				rows++;
				if(flag) {
					first = line;
					flag = false;
				}else {
					data = line.split(",");
					classList.add(data[data.length-1]);
					
					for(int i =0;i<data.length-1;i++) {
						theList.add(new ArrayList<Float>());
						theList.get(i).add(Float.parseFloat(data[i]));
					}
				}
			}  
			br.close();
		}   
		catch (IOException e)   {  
			e.printStackTrace();  
		}  
		
		columns = data.length;
		
		//Copy from trainig set to es
		ArrayList<ArrayList<Float> > es = new ArrayList<ArrayList<Float>>(theList);
		ArrayList<String> tempClassList = new ArrayList<String>(classList);
        
		System.out.println("Please wait...(It may take up to 4 minutes)");
		
		//Calculate and remove values
		long remover = 0;
        for(long m=0;m<rows-1;m++) {
	        ArrayList<ArrayList<Float> > tempList = findDistances(theList,(int) m);
	        
	        Collections.sort(tempList, new Comparator<ArrayList<Float>>() {
	        	@Override
	        	public int compare(ArrayList<Float> one, ArrayList<Float> two) {
	        			return one.get(0).compareTo(two.get(0));
	        	}
	        });
	        
	        int count = 0;
	        for(int i=1;i<k+1;i++) {
	        	if(classList.get((int) m).equals(classList.get(Math.round(tempList.get(i).get(1))))) {
	        		count++;
	        	}
	        }
	        
	        if(count<(k/2)) {
	        	tempClassList.remove(remover);
	        	for(int y=0;y<4;y++) {
	        		es.get(y).remove(remover);
	        		remover = remover -1;
	        	}
	        }
	        remover++;
        }
   
        //Save to csv file
        try (PrintWriter writer = new PrintWriter("edited-file.csv")) {
		      StringBuilder sb = new StringBuilder();

		      sb.append(first);
		      sb.append('\n');
		      
		      for(long y=0;y<remover;y++) {
		    	  for(int i=0;i<columns-1;i++) {
		    		  sb.append(es.get(i).get((int) y));
		    		  sb.append(',');
		    	  }
		    	  sb.append(tempClassList.get((int) y));
		    	  sb.append('\n');
		      }
		      writer.write(sb.toString());
		      System.out.println("The edited file is in the program's folder");

	   } catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
	   }
 
	}
	
	//Calculate distances
	public static ArrayList<ArrayList<Float> > findDistances(ArrayList<ArrayList<Float> > theList,int position) {
		 ArrayList<ArrayList<Float> > tempList = new ArrayList<ArrayList<Float> >();
	        float distance = 0;
	        float sum = 0;
	        
	        for(long i=0;i<rows-1;i++) {
	        	sum = 0;
	        	for(int y=0;y<columns-1;y++) {
	        		sum = (float) (sum + Math.pow((theList.get(y).get((int) i))-(theList.get(y).get(position)), 2));
	        	}
	        	distance = (float) Math.sqrt(sum);
	        	tempList.add(new ArrayList<Float>());
	        	tempList.get((int) i).add(distance);
	        	tempList.get((int) i).add((float) i);
	        }
		return tempList;
	}

}
