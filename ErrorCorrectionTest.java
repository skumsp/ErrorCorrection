package ErrorCorrection;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import net.sourceforge.argparse4j.*;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;

public class ErrorCorrectionTest {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		                
/*		String folder_name  = args[0];
		int k = Integer.parseInt(args[1]);
		int lt = Integer.parseInt(args[2]);
		int nIter = Integer.parseInt(args[3]);
                int toFindHapl = Integer.parseInt(args[4]);
                int errorsseglen = Integer.parseInt(args[5]);*/
                
                String folder_name  = "G:" + File.separator + "AMD_outbreak" + File.separator + "test" + File.separator + "input";
                
                File folder = new File(folder_name);
                
                if (!folder.exists())
                {
                    System.out.println("No such folder");
                    System.exit(1);
                }
                File[] list_files = folder.listFiles();
                
                for (int i = 0; i < list_files.length; i++)
                {
                    
                    int k = 25;
                    int lt = 50;
                    int nIter = 3;
                    int toFindHapl = 1;
                    int errorsseglen = 0;
                    String refFile_name = "G:" + File.separator + "AMD_outbreak" + File.separator + "test" + File.separator + "ref_HVR1.fas";
                
                    DataSet refs = new DataSet(refFile_name);

                    int gapop = 15;
                    int gapext = 6;


                    int mErPerc = 50;
                    int toClust = 1;
                    int dominparamonenucl = 5;
                    int nucldiffparam = 1;
                    int dominparamgen = 25;
                    int dominparampostpr = 25; //30
                    int minNReads = 10;
                    boolean toPrintStat = false;

                    int maxz = 3;
                    int kmin = k-15;
                    boolean toCalcHapl = false;
                    boolean toDelUncor = false;
                    boolean toPostprocessHeur = false; 


                    String dset_file = list_files[i].getPath();

                    DataSet ds = new DataSet(dset_file);
                    ds.setK(k);
                    ds.setLenThr(lt);
                    ds.setMaxAllErrorsPerc(mErPerc);
                    ds.setFindErrorsSeglen(errorsseglen);
                    
                    ds.fixDirectionRefParallel(refs, gapop, gapext);

                    Corrector cr = new Corrector(ds);
                    cr.setMaxz(maxz);
                    cr.setNIter(nIter);
                    cr.setToClust(toClust);
                    cr.setKmin(kmin);
                    cr.setToRemoveAllUncorrect(toDelUncor);
                    cr.setToFindHapl(toCalcHapl);
                    cr.setToPrintStat(toPrintStat);
                    cr.setToPostprocessHeur(toPostprocessHeur);
                    cr.setMinNReads(minNReads);
                    cr.run();
                    ds = cr.CorrectedReads();

                    ds.PrintCorrectedReads(dset_file+"_corrected.fas");
                    if (toFindHapl == 1)
                    {
                        ds.PrintHaplotypes(dset_file+"_haplotypes.fas");
    //                    cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas",15,6.6, dominparampostpr);
    //                    cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas_postprocessed.fas",1,1);
    //                    cr.printRevComp(dset_file_name +"_haplotypes.fas_postprocessed.fas");
                    }

                    System.out.println("Finished stage 1!");


                    // SECOND RUN
                    maxz = 0;
                    nIter = 0;
                    toPrintStat = false;
                    toDelUncor = true;
                    toPostprocessHeur = false; //???
                    dset_file = dset_file+"_corrected.fas";
                    dset_file = dset_file;
                    toFindHapl = 1;
                    toCalcHapl = true;

                    ds = new DataSet(dset_file);
                    ds.setK(k);
                    ds.setLenThr(lt);
                    ds.setMaxAllErrorsPerc(mErPerc);
                    ds.setFindErrorsSeglen(errorsseglen);
                    minNReads = 1;


                    cr = new Corrector(ds);
                    cr.setMaxz(maxz);
                    cr.setNIter(nIter);
                    cr.setToClust(toClust);
                    cr.setKmin(kmin);
                    cr.setToRemoveAllUncorrect(toDelUncor);
                    cr.setToFindHapl(toCalcHapl);
                    cr.setToPrintStat(toPrintStat);
                    cr.setToPostprocessHeur(toPostprocessHeur);
                    cr.setMinNReads(minNReads);
                    cr.run();
                    ds = cr.CorrectedReads();

                    ds.PrintCorrectedReads(dset_file+"_corrected.fas");
                    ds.PrintHaplotypes(dset_file+"_haplotypes.fas");
                    if (toFindHapl == 1)
                    {
                        ds.PrintHaplotypes(dset_file+"_haplotypes.fas");
    /*                    cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas",15,6.6,dominparampostpr);
                        cr.printRevComp(dset_file_name +"_haplotypes.fas_postprocessed.fas");
                        System.out.println(dset_file_name);
                        cr.postprocessHaplotypesPairwise(dset_file_name +"_haplotypes.fas_postprocessed.fas_RevComp.fas", 15, 6.6, dominparamonenucl, dominparamgen, nucldiffparam);
                        cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas_postprocessed.fas_RevComp.fas_PostprocPair.fas",15,6.6,dominparampostpr);
                        cr.postprocessHaplotypesPairwise(dset_file_name + "_haplotypes.fas_postprocessed.fas_RevComp.fas_PostprocPair.fas_postprocessed.fas", 15, 6.6, dominparamonenucl, dominparamgen, nucldiffparam);*/
                    }

                    System.out.println("Finished stage 2!");
                }
                System.exit(0);
	}

}
