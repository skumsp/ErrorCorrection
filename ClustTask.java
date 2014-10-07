/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

/**
 *
 * @author kki8
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;

public class ClustTask implements Callable {
   public Read r;
   public int count;
   public ClustTask() 
   {
   }
   public ClustTask(Read r1) 
   { 
       r = r1;
   }
    public ClustTask(Read r1, int n) 
   { 
       r = r1;
      count = n;
   }
   public void setRead(Read r1)
   {
       r = r1;
   }

   public Object call() throws FileNotFoundException, IOException 
   {
     
			 File f = new File("kmer" + count + "data.txt");
			 FileWriter fw = new FileWriter(f);
			 fw.write(r.kmers.size() + " " + 1 + "\n");
			 for (Kmer km : r.kmers)
				 fw.write(km.getCount() + "\n");
			 fw.close();
			 
			 Runtime run=Runtime.getRuntime(); 
			 Process p=null;
                         String exPath = "fams"+File.separator+"fams 0 0 200 " + "kmer" + count + "data ."+ File.separator;
			 p=run.exec(exPath); 
			 try {
				p.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
			 if (p.exitValue()!= 0)
			 {
				 System.out.println("Error in clustering program");
				 return -1;
			 }
			 
			 File fout = new File("out_" + "kmer" + count + "data.txt");
			 
			 FileReader fr = new FileReader(fout);
			 BufferedReader br = new BufferedReader(fr);
			 
			 String s = br.readLine();
			 int i = 0;
			 while(s!= null)
			 {
				 r.kmers.get(i).setClusterParam(Double.parseDouble(s));
				 i++;
				 s = br.readLine();
			 }
                         br.close();
                         fr.close();
			 
			 f.delete();
                         f = new File("pilot_200_" + "kmer" + count + "data.txt");
                         f.delete();
                         f = new File("modes_" + "kmer" + count + "data.txt");
                         f.delete();
                         f = new File("out_kmer" + count + "data.txt");
                         f.delete();
      return count;
    }
}
    

