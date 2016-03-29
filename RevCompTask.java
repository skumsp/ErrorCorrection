/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;

/**
 *
 * @author kki8
 */
public class RevCompTask implements Callable{
   Read r;
   DataSet refs;
   int gapop;
   int gapext;
   public RevCompTask() 
   {
   }
   public RevCompTask(Read r1, DataSet refs1,int gapop1,int gapext1) 
   { 
       r = r1;
       refs = refs1;
       gapop = gapop1;
       gapext = gapext1;
   }

   public Object call() throws FileNotFoundException, IOException, CompoundNotFoundException 
   {
       int bestDist = Integer.MAX_VALUE;
       int toRev = 0;
       for (Read ref : refs.reads)
       {
            StringTokenizer st = new StringTokenizer(ref.name,"_");
            String genot = st.nextToken();
            int dDir = r.calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
            int dRev = r.getRevComp().calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
            bestDist = Math.min(bestDist, dDir);
            bestDist = Math.min(bestDist, dRev);
            if (dDir == bestDist)
            {
                toRev = 0;
                r.setGenotype(genot);
            }
            if (dRev == bestDist)
            {
                toRev = 1; 
                r.setGenotype(genot);
            }
       }
       if (toRev == 1)
     		r.RevComp();
       return 0;

   }
    
}
