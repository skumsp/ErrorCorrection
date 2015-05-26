/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ErrorCorrection;

import ErrorCorrection.DataSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author kki8
 */
public class CalcRealErrorsStatRun 
{
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException 
    {
                String refName = "ref_clones_no_primers.fas";
                DataSet ds2 = new DataSet(refName,'c');
                ds2.PrintReadsSeparateFiles();
                
                
                String cloneMIDaddr = "clonesMID.txt";
                BufferedReader br = new BufferedReader(new FileReader(cloneMIDaddr));
                HashMap<Integer,Integer> hm = new HashMap();
                String s = br.readLine();
                while (s!=null)
                {
                    StringTokenizer st = new StringTokenizer(s," \t");
                    int clone = Integer.parseInt(st.nextToken());
                    int mid = Integer.parseInt(st.nextToken());
                    hm.put(mid, clone);
                    s = br.readLine();
                }
                
                File folder = new File("Illumina_no_chimeras");
                File[] list_files = folder.listFiles();
                
                List< Future > futuresList = new ArrayList< Future >(list_files.length);
                int nrOfProcessors = Runtime.getRuntime().availableProcessors();
                ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

                for (int i = 0; i < list_files.length; i++)
                {
                    String dset_file = list_files[i].getPath();
                    String dset_file_name = list_files[i].getName();
                    StringTokenizer st = new StringTokenizer(dset_file_name,"_-");
                    for (int j = 1; j<=2; j++)
                        st.nextToken();
                    s = st.nextToken().substring(1);
                    int mid = Integer.parseInt(s);
                    int clone = hm.get(mid);
                    
                    String cloneFile = refName + "_" + clone + ".seq.fas";
                    DataSet ds = new DataSet(dset_file,"ET");
                    calcRealErrorsTask ts = new calcRealErrorsTask(ds,cloneFile);
                    futuresList.add(eservice.submit(ts));
 //                   ds.fixDirectionGenotypingRefParallel(refs, 15, 6);
 //                   ds.calculateRealErrorsStat(cloneFile, 10, 15, 6.6);
 //                   ds.PrintUniqueReads(dset_file + "_unique.fas");
                }
                
                 
                  Object taskResult;
                  for(Future future:futuresList) 
                  {
                        taskResult = future.get();                          
                  }
        
    }
}
