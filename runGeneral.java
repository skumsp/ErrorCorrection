/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import net.sourceforge.argparse4j.*;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.*;

/**
 *
 * @author kki8
 */
public class runGeneral {
     public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		                
/*		String folder_name  = args[0];
		int k = Integer.parseInt(args[1]);
		int lt = Integer.parseInt(args[2]);
		int nIter = Integer.parseInt(args[3]);
                int toFindHapl = Integer.parseInt(args[4]);
                int errorsseglen = Integer.parseInt(args[5]);*/
                
                String folder_name_input  = "forbi";
                String outFolder = "forbi_output";
                File fl = new File(folder_name_input);
                
                String folder_name  = fl.getPath();
                if (outFolder.length()  == 0)
                    outFolder = folder_name;
                else
                {
                    File f = new File(outFolder);
                    f.mkdir();
                }
                File fl_ref = new File("ref_HVR1.fas");
                
                String refFile_name = fl_ref.getPath();
                
                
                
                DataSet refs = new DataSet(refFile_name,'c');
                
                int gapop = 15;
                int gapext = 6;
                int dominparamgen = 25;
                int dominparampostpr = 25; //30
                int k = 25;
                int nIter = 3;
                int errorsseglen = 0;
                int toFindHapl = 1;
                int nProc = Runtime.getRuntime().availableProcessors();
                String alignmethod = ""; // "Muscle" or "Clustal"
                int lt = 150;
	
                    
		int mErPerc = 50;
		int toClust = 1;
                int minNReads = 50;
                boolean toPrintStat = false;

                int maxz = 3;
		int kmin = k-15;
		boolean toCalcHapl = false;
                boolean toDelUncor = false;
                boolean toPostprocessHeur = false; 
                
                
                File folder = new File(folder_name);
                
                if (!folder.exists())
                {
                    System.out.println("No such folder");
                    System.exit(1);
                }
                File[] list_files = folder.listFiles();
                
                for (int i = 0; i < list_files.length; i++)
                {
                    

                    String dset_file = list_files[i].getPath();
                    String dset_file_name = list_files[i].getName();
                    
/*                    StringTokenizer st = new StringTokenizer(dset_file_name,".");
                    String tag = st.nextToken();
                    
                    boolean isGhost = dset_file_name.contains("[");
                    String ghostpref = "";
                    String ghostsuff = "";
                    if (isGhost)
                    {
                        StringTokenizer stG = new StringTokenizer(dset_file_name,"[]");
                        ghostpref = stG.nextToken();
                        tag = stG.nextToken();
                        ghostsuff = stG.nextToken();
                    }*/


                    DataSet ds = new DataSet(dset_file,lt);
                    ds.setK(k);
                    ds.setAvProc(1);
                    ds.setLenThr(lt);
                    ds.setMaxAllErrorsPerc(mErPerc);
                    ds.setFindErrorsSeglen(errorsseglen);
                    ds.setFileNameShort(dset_file_name);
                   
                    
//                    ds.fixDirectionGenotypingRefParallel(refs, gapop, gapext);
//                    ds.PrintUniqueReadsWithTagGenotype(folder_name_input + File.separator + "reads.fas", "read");
//                    HashMap<String, ErrorCorrection.DataSet> hm = ds.separateGenotypes();
//                    for (Map.Entry me : hm.entrySet())
                    {
                                               
 //                       String genot = (String) me.getKey();
 //                       String dset_file_name_short = dset_file_name + "_" + genot;
 //                       ds = (ErrorCorrection.DataSet) me.getValue();
                        String tag = dset_file_name;
                        String genot = "";
                        ds.setK(k);
                        ds.setAvProc(1);
                        ds.setLenThr(lt);
                        ds.setMaxAllErrorsPerc(mErPerc);
                        ds.setFindErrorsSeglen(errorsseglen);
//                        ds.setFileNameShort(dset_file_name_short);
                        
                        String dset_file_seqs = outFolder + File.separator +tag+"_"+genot+"_corrected.fas";
                        String dset_file_hapl = outFolder + File.separator +tag+"_"+genot+"_haplotypes.fas";
                        
                        ds.PrintUniqueReads(dset_file_seqs);

                        
                        if (ds.reads.size() < minNReads)
                        {
                            ds.PrintUniqueReads(dset_file_seqs);
                            ds.findHaplotypes();
                            ds.PrintHaplotypes(dset_file_hapl);
                            String logfilename = outFolder + File.separator + ds.file_name_short + "_log.txt";
                            FileWriter fw = new FileWriter(logfilename, true);
                            fw.write("Too few reads \n");
                            fw.close();
                            continue;
                        }
                        

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
                        cr.setOutfolder(outFolder);
                        cr.run();
                        ds = cr.CorrectedReads();

    //                    ds.PrintCorrectedReads(outFolder + File.separator +dset_file_name+"_corrected.fas");
    //                    ds.PrintUniqueReadsWithTagGenotype(outFolder + File.separator +dset_file_name+"_corrected.fas", tag);
                        System.out.println("Finished stage 1!");


                        // SECOND RUN
                        int maxz_postp = 0;
                        int nIter_postp = 0;
                        boolean toPrintStat_postp = false;
                        boolean toDelUncor_postp = true;
                        boolean toPostprocessHeur_postp = false; //???

                        int toFindHapl_postp = 1;
                        boolean toCalcHapl_postp = true;

    //                    ds = new DataSet(dset_file);
                        ds = new DataSet(ds);
                        ds.freqThr = -1;
                        ds.setK(k);
                        ds.setAvProc(nProc);
                        ds.setLenThr(lt);
                        ds.setMaxAllErrorsPerc(mErPerc);
                        ds.setFindErrorsSeglen(errorsseglen);
                        ds.setFileNameShort(dset_file_name);
                        int minNReads_postp = 1;


                        cr = new Corrector(ds);
                        cr.setMaxz(maxz_postp);
                        cr.setNIter(nIter_postp);
                        cr.setToClust(toClust);
                        cr.setKmin(kmin);
                        cr.setToRemoveAllUncorrect(toDelUncor_postp);
                        cr.setToFindHapl(toCalcHapl_postp);
                        cr.setToPrintStat(toPrintStat_postp);
                        cr.setToPostprocessHeur(toPostprocessHeur_postp);
                        cr.setMinNReads(minNReads_postp);
                        cr.setOutfolder(outFolder);
                        cr.run();
                        ds = cr.CorrectedReads();

    //                    ds.PrintCorrectedReads(dset_file+"_corrected.fas");
                        ds.PrintUniqueReadsWithTagGenotype(dset_file_seqs, tag);
                        if (toFindHapl_postp == 1)
                        {
                            ds.PrintHaplotypes(dset_file_hapl);
        /*                    cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas",15,6.6,dominparampostpr);
                            cr.printRevComp(dset_file_name +"_haplotypes.fas_postprocessed.fas");
                            System.out.println(dset_file_name);
                            cr.postprocessHaplotypesPairwise(dset_file_name +"_haplotypes.fas_postprocessed.fas_RevComp.fas", 15, 6.6, dominparamonenucl, dominparamgen, nucldiffparam);
                            cr.postprocessHaplotypes(dset_file_name+"_haplotypes.fas_postprocessed.fas_RevComp.fas_PostprocPair.fas",15,6.6,dominparampostpr);
                            cr.postprocessHaplotypesPairwise(dset_file_name + "_haplotypes.fas_postprocessed.fas_RevComp.fas_PostprocPair.fas_postprocessed.fas", 15, 6.6, dominparamonenucl, dominparamgen, nucldiffparam);*/
                        }

                        System.out.println("Finished stage 2!");
                    }
                }
                System.exit(0);

                                
    }
    
}
