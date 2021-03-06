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
public class RevCompGenTask implements Callable 
{
    Read r;
    DataSet refs;
    int gapop;
    int gapext;
    double refDistPerc;
    
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
   
    public RevCompGenTask(Read r1, DataSet refs1,int gapop1,int gapext1, double d) 
   { 
       r = r1;
       refs = refs1;
       gapop = gapop1;
       gapext = gapext1;
       refDistPerc = d;
   }

   public Object call() throws FileNotFoundException, IOException, CompoundNotFoundException 
   {
       double bestDist = Double.MAX_VALUE;
       int toRev = 0;
       int genRef = 0;
       for (int i =0; i < refs.reads.size(); i++)
       {
            Read ref = refs.reads.get(i);
//            int dDir = r.calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
//            int dRev = r.getRevComp().calcEditDistAbsAlignWithGaps(ref, gapop, gapext);
//            double dDir = r.calcEditDistAbsAlign(ref, gapop, gapext);
//            double dRev = r.getRevComp().calcEditDistAbsAlign(ref, gapop, gapext);
            double dDir = r.calcEditDistAbsAlignWithGapsIgnoreTails(ref, gapop, gapext);
            double dRev = r.getRevComp().calcEditDistAbsAlignWithGapsIgnoreTails(ref, gapop, gapext);
//            double dDir = r.calcEditDistRelAlign(ref, gapop, gapext);
//            double dRev = r.getRevComp().calcEditDistRelAlign(ref, gapop, gapext);
//            double dDir = -r.calcAlignScore(ref, gapop, gapext);
//            double dRev = -r.getRevComp().calcAlignScore(ref, gapop, gapext);
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
        r.distgen = bestDist;
/*       if (bestDist <= this.refDistPerc)
       {
            StringTokenizer st = new StringTokenizer(refs.reads.get(genRef).name,"_");
            r.setGenotype(st.nextToken());
       }
       else
       {
           r.setGenotype("x");
       }*/
       return 0;

   }
}
