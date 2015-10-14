/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

/**
 *
 * @author kki8
 */
public class RevCompGenTask implements Callable 
{
    Read r;
    DataSet refs;
    int gapop;
    int gapext;
    
    public RevCompGenTask() 
    {
        
    }
   public RevCompGenTask(Read r1, DataSet refs1,int gapop1,int gapext1) 
   { 
       r = r1;
       refs = refs1;
       gapop = gapop1;
       gapext = gapext1;
   }

   public Object call() throws FileNotFoundException, IOException 
   {
       int bestDist = Integer.MAX_VALUE;
       int toRev = 0;
       int genRef = 0;
       for (int i =0; i < refs.reads.size(); i++)
       {
            Read ref = refs.reads.get(i);
//            int dDir = r.calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
//            int dRev = r.getRevComp().calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
            int dDir = (int) r.calcEditDistAbsAlign(ref, gapop, gapext);
            int dRev = (int) r.getRevComp().calcEditDistAbsAlign(ref, gapop, gapext);
            bestDist = Math.min(bestDist, dDir);
            bestDist = Math.min(bestDist, dRev);
            if (dDir == bestDist)
            {
                toRev = 0;
                genRef = i;
            }
            if (dRev == bestDist)
            {
                toRev = 1; 
                genRef = i;
            }
       }
       if (toRev == 1)
     		r.RevComp();
       StringTokenizer st = new StringTokenizer(refs.reads.get(genRef).name,"_");
       r.setGenotype(st.nextToken());
       return 0;

   }
}
