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
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;

/**
 *
 * @author kki8
 */
public class ClipRefTask implements Callable 
{
    Read r;
    DataSet refs;
    int gapop;
    int gapext;
    
    public ClipRefTask () 
    {
        
    }
   public ClipRefTask (Read r1, DataSet refs1,int gapop1,int gapext1) 
   { 
       r = r1;
       refs = refs1;
       gapop = gapop1;
       gapext = gapext1;
   }
   

   public Object call() throws FileNotFoundException, IOException, CompoundNotFoundException 
   {
       double bestDist = Double.MAX_VALUE;
       int genRef = 0;
       for (int i =0; i < refs.reads.size(); i++)
       {
            Read ref = refs.reads.get(i);
//            int dDir = r.calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
//            int dRev = r.getRevComp().calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
//            double dDir = r.calcEditDistAbsAlign(ref, gapop, gapext);
//            double dRev = r.getRevComp().calcEditDistAbsAlign(ref, gapop, gapext);
            double dDir = r.calcEditDistAbsAlignWithGapsIgnoreTails(ref, gapop, gapext);
            bestDist = Math.min(bestDist, dDir);
            if (dDir == bestDist)
            {
                genRef = i;
            }
       }
       
       r.clipToRef(refs.reads.get(genRef), gapop, gapext); 
       return 0;

   }
    
}
