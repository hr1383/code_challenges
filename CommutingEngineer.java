import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// https://www.codeeval.com/open_challenges/90/
public class CommutingEngineer {

     List<Startup> startupList = new ArrayList<Startup>();
     List<List<Startup>> graph = new ArrayList<List<Startup>>();
	private static int minIndex = -1;
	private static double minDistance = Double.MAX_VALUE;

	public static void main(String[] args) {
		new CommutingEngineer().findShortestPath(args[0]);
	}
	
	private void findShortestPath(String fileName){
		File file = new File(fileName);
		long time = System.currentTimeMillis();
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		
		}
		
		String line = "";
		//read the file and create Startup Object
		try {
			while ((line = in.readLine()) != null) {
				String[] lineArray = line.split("\\|");
				String index = lineArray[0];
				String startup = lineArray[1].split("\\(")[0];
				String restString = lineArray[1].split("\\(")[1];
				String latitude = restString.split(",")[0];
				String longitude = restString.split(",")[1].substring(0,
						restString.split(",")[1].length() - 2);
				startupList.add(new Startup(startup, latitude, longitude, index));
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		if (startupList.size() == 0)
			return;
		
		generateAllPermutationIteratively(startupList.get(0),startupList.subList(1, startupList.size()));
		traverseAllCombination(graph);
		
		if (minIndex != -1) {
			List<Startup> pathList = graph.get(minIndex);
			for (Startup path : pathList) {
				System.out.println(path.getIndex());
			}
		}
		System.out.println("Total time " + (System.currentTimeMillis() - time));
	}

	/**
	 * traverse through all combination and find the minimum path
	 * @param graphList
	 */
	private  void traverseAllCombination(List<List<Startup>> graphList) {
		int j = 0;
		for (List<Startup> graph : graphList) {
			double distance = 0.0;
			for (int i = 0; i < graph.size(); i++) {
				if (i != graph.size() - 1)
					distance += calculateDistance(graph.get(i),graph.get(i + 1));
			}
			if (distance < minDistance) {
				minDistance = distance;
				minIndex = j;
			}
			j++;
		}
	}

	/**
	 *source quickperm.org. The iterative method is always better than recursive. 
	 *Using iteration the memory came down by 40 MB and processing time less by 1 sec
	 *
	 * 
	 */
	
	public void generateAllPermutationIteratively(Startup source, List<Startup> list){
		int i=0;
		int N=list.size();
		List<Startup> newList = new ArrayList<Startup>();
        newList.add(source);
        newList.addAll(list);
		graph.add(newList);
		int p[] = new int [N];
		   for(i = 0; i < N; i++) {  // initialize arrays; a[N] can be any type
		      p[i] = 0;      
		   }
		   i = 1;  
		   while(i < N) {
		      if (p[i] < i) {
		        int j = i % 2 * p[i];  
		        Startup  tmp = list.get(j);         
		         list.set(j,list.get(i)) ;
		         list.set(i, tmp);
		         newList = new ArrayList<Startup>();
		         newList.add(source);
		         newList.addAll(list);
		         graph.add(newList);
		         p[i]++;             
		         i = 1;              
		      } else {               
		         p[i] = 0;          
		         i++;               
		      } // if (p[i] < i)
		   } // while(i < N)
	} //
	
	
   /**
    * source from stackoverflow
    * @param firstStartup
    * @param secondStartup
    * @return
    */
	private  double calculateDistance(Startup firstStartup,
			Startup secondStartup) {
		double radius = 3961.3; // #earth's mean radius in miles
		double lat = radius(firstStartup.getLatitude()
				- secondStartup.getLatitude());
		double longitude = radius(firstStartup.getLongitude()
				- secondStartup.getLongitude());
		double a = Math.sin(lat / 2)
				* Math.sin(lat / 2)
				+ Math.cos(radius(firstStartup.getLatitude())
						* Math.cos(secondStartup.getLatitude()))
				* Math.sin(longitude / 2) * Math.sin(longitude / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return radius * c;
	}
	
	private  double radius(double value) {
		return value / 180 * Math.PI;
	}
	
	/**
	 * didnt use recursive method because of being too slow
	 * generate all possible combination
	 * @param source
	 * @param newlist
	 * @param startupList
	 */
	private  void generatAllPermutation(Startup source,List<Startup> newlist, List<Startup> startupList) {
		if (startupList.size() == 0) {
			newlist.add(0, source);
			graph.add(newlist);
			return;
		}
		for (int i = 0; i < startupList.size(); i++) {
			List<Startup> tempList2 = new ArrayList<Startup>(newlist);
			tempList2.add(startupList.get(i));
			List<Startup> tempList = new ArrayList<Startup>(startupList);
			tempList.remove(i);
			generatAllPermutation(source, tempList2, tempList);
		}
	}

}

class Startup {

	private String startup;
	private double longitude;
	private double latitude;
	private String index;

	public Startup(String startup, String latitude, String longitude,
			String index) {
		super();
		this.startup = startup;
		this.longitude = Double.valueOf(longitude);
		this.latitude = Double.valueOf(latitude);
		this.index = index;
	}

	public String getStartup() {
		return startup;
	}

	public void setStartup(String startup) {
		this.startup = startup;
	}

	public double getLongitude() {
		return longitude;
	}

	public String toString() {

		return startup;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
}
