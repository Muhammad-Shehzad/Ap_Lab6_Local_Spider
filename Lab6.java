import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JOptionPane;

public class Lab6  {
	  private WorkQueue workQ;
	  static int i = 0;
	  
	static String directory="C:\\Users\\shehz\\Desktop\\New";
	
    public static void main(String[] args) throws IOException {  
    	  Lab6 fc = new Lab6();
    	  
    	//  now start all of the worker threads
    	 
    	  int N = 3;
    	  ArrayList<Thread> thread = new ArrayList<Thread>(N);
    	  for (int i = 0; i < N; i++) {
    	   Thread t = new Thread(fc.createWorker());
    	   thread.add(t);
    	   System.out.println("T added"+i);
    	   t.start();
    	  }
    	 
    	//  now place each directory into the workQ
    	 
    	  fc.processDirectory(directory);
    	 
    	//  indicate that there are no more directories to add
    	 
    	  fc.workQ.finish();
    	 
    	  for (int i = 0; i < N; i++){
    	   try {
    	    thread.get(i).join();
    	   } catch (Exception e) {};
    	  }
        } //end main
    public void processDirectory(String dir) {
    	   try{
    	   File file = new File(dir);
    	   if (file.isDirectory()) {
    	    String entries[] = file.list();
    	    if (entries != null)
    	     workQ.add(dir);
    	 
    	    for (String entry : entries) {
    	     String subdir;
    	     if (entry.compareTo(".") == 0)
    	      continue;
    	     if (entry.compareTo("..") == 0)
    	      continue;
    	     if (dir.endsWith("\\"))
    	      subdir = dir+entry;
    	     else
    	      subdir = dir+"\\"+entry;
    	     processDirectory(subdir);
    	    }
    	   }}catch(Exception e){}
    	 }
    
    public Lab6() {
    	  workQ = new WorkQueue();
    	 }
    	 
    	 public Worker createWorker() {
    	  return new Worker(workQ);
    	 } 
    
   /*
    * Method to search user's entered string in file Name and file content
    */
    public static String Crawling(HashMap hashMap, String input) throws IOException{           
            //to traverse the map
    	System.out.println("Crawling");
            Set set = hashMap.entrySet();
            Iterator iterator = set.iterator();
            String res="";
            String resFile="";
            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                
               String fileName=(String)entry.getKey();
               
                   if(fileName.contains(input)){
                  //  resFile+="\nFile Names have this string: \n\t"+ fileName+"\n";  
                	   resFile+=fileName+"\n";
                   }
           
               String val = (String)entry.getValue();
                File file = new File(val); 
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = null;
                while ((line = br.readLine()) != null) {
                        if(line.contains(input)){
                         res+=val+"\n";  
                        }
                }
            } 
			return res+resFile;   
    }


private class Worker implements Runnable {
	 
	  private WorkQueue queue;
	 
	  public Worker(WorkQueue q) {
	   queue = q;
	  }
	 
	//  since main thread has placed all directories into the workQ, we
	//  know that all of them are legal directories; therefore, do not need
	//  to try ... catch in the while loop below
	 
	  public void run() {
	   String name;
	   HashMap<String,String> hashMap = new HashMap();
	   while ((name = queue.remove()) != null) {
	    File file = new File(name);
	    String entries[] = file.list();
	    if (entries == null)
	     continue;
	    for (String entry : entries) {
	     if (entry.compareTo(".") == 0)
	      continue;
	     if (entry.compareTo("..") == 0)
	      continue;
	     String fn = name + "\\" + entry; //get the file path
	     System.out.println("File ka nam "+entry);
	     System.out.println("File ka path "+fn);
	     hashMap.put(entry, fn);
	    }
	   } //end while
   //    while(true){
       String input = JOptionPane.showInputDialog("Enter the keyword you want to search in files \n Press / to exit: ");
       if(input.equals("/")){
           return;
       }
       String res = null;
	try {
		res = Crawling(hashMap, input);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}// searching string  
       JOptionPane.showMessageDialog(null,"The string exists in these files contents: \n\t"+res);
       
    //   }
       
	 } //end method run()
}
    }//end class lab6
    


