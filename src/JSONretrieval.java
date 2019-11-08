import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class JSONretrieval {
	public static void main(String args[]) throws FileNotFoundException, IOException, ParseException
	{
		Scanner in=new Scanner(System.in);
		System.out.println("Enter the Folder Path: ");
		String folderpath=in.nextLine();
		System.out.println("Enter the Input file base name: ");
		String inputfile=in.next();
		System.out.println("Enter the output file base name: ");
		String out=in.next();
		System.out.println("Enter the Maximum size of the file(Bytes)");
		int l=in.nextInt();
		File folder = new File(folderpath);
		File[] listOfFiles = folder.listFiles();
		int[] lengthOfFiles=new int[1600];
		Arrays.sort(listOfFiles);
	    for(int i=0;i<listOfFiles.length;i++)
	    {  
	    	lengthOfFiles[i]=(int)getFileSizeBytes(listOfFiles[i]);
	    }
	    HashMap sizes=new HashMap(listOfFiles.length);
	    for(int i=0;i<listOfFiles.length;i++)
	    {  
	    	if (listOfFiles[i].getName().startsWith(inputfile)) 
	    	{
	    		sizes.put(listOfFiles[i].getName(),lengthOfFiles[i] );
	    	}
	    }
	    Map<String, Integer> hm1 = sortByValues(sizes);
	    Map<String, Integer> hm2 = sortByValues(sizes);
	    int sum=0;
	    for (Entry<String, Integer> entry : hm2.entrySet()) {
	    	if(sum+entry.getValue()<l)
	    	sum+=entry.getValue();
	    	else
	    		hm1.remove(entry.getKey());
	    	
	    }
	     hm1 = sortBykeys(hm1);
        Iterator<Map.Entry> itr1 = null;
		JSONArray employeeList = new JSONArray();
		JSONObject employeeDetails = new JSONObject();
		Map m = null;
		System.out.println("Merged Documents are");
		for (Entry<String, Integer> entry : hm1.entrySet()) {
		  if (entry.getKey().startsWith(inputfile)) 
		  {   System.out.println(entry.getKey()); 
			  Object obj = new JSONParser().parse(new FileReader(folderpath+"\\"+entry.getKey()));
			  JSONObject jo=(JSONObject)obj;
			  String key=null;
			  Iterator<?> keys = jo.keySet().iterator();
			  while(keys.hasNext()) {
			  key=(String)keys.next();
			  }
			  JSONArray ja = (JSONArray)jo.get(key);
	          
		        // iterating phoneNumbers 
		        Iterator itr2 = ja.iterator(); 
		          
		        while (itr2.hasNext())  
		        { 
		            itr1 = ((Map) itr2.next()).entrySet().iterator();
		            m=new HashMap(200);
		            while (itr1.hasNext()) { 
		                Map.Entry pair = itr1.next(); 
		                m.put(pair.getKey(),pair.getValue());
		         } 
		            employeeList.add(m);
		        } 
		        employeeDetails.put(key,employeeList);
		     }
		}
		
		try (FileWriter file = new FileWriter(folderpath+"\\"+out+".json")) {

            file.write(employeeDetails.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
		File f=new File(folderpath+"\\"+out+".json");
		System.out.println("Merged JSON file created @ "+f.getParent()+" The size of the document is "+(int)getFileSizeBytes(f)+" bytes");
	}

	private static Map<String, Integer> sortBykeys(Map<String, Integer> hm2) {
		  List<Map.Entry<String, Integer> > list = 
	               new LinkedList<Map.Entry<String, Integer> >(hm2.entrySet()); 
	  
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
	            public int compare(Map.Entry<String, Integer> o1,  
	                               Map.Entry<String, Integer> o2) 
	            { 
	                return (o1.getKey()).compareTo(o2.getKey()); 
	            } 
	        }); 
	          
	         
	        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
	        for (Map.Entry<String, Integer> aa : list) { 
	            temp.put(aa.getKey(), aa.getValue()); 
	        } 
	        return temp; 
	}

	private static Map<String, Integer> sortByValues(HashMap sizes) {
		 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(sizes.entrySet()); 
  
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (-(o1.getValue()).compareTo(o2.getValue())); 
            } 
        }); 
          
         
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
	}

	private static long getFileSizeBytes(File f) {
	
		return f.length();
	}

}
