package ErrorCorrection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class ErrorCorrection {
        private static final String K_PARAMETER = "K";
        private static final String I_PARAMETER = "I";
        private static final String L_PARAMETER = "L";
        private static final String H_PARAMETER = "H";
        private static final String LEN_PARAMETER = "len";
        private static final String NPROC_PARAMETER = "nproc";
        private static final String READS_PARAMETER = "in";
        private static final String OUT_PARAMETER = "out";
        private static final String REFERENCE_PARAMETER = "refs";
        private static final String DOMINGEN_PARAMETER = "domgen";
        private static final String DOMINPOSTPROC_PARAMETER = "dompostproc";
        private static final String CLUSTAL_PARAMETER = "clustalw";
        private static final String MUSCLE_PARAMETER = "muscle";
        static final String CLUSATL = "Clustal";
        static final String MUSCLE = "Muscle";
        static File env_path;
        static {
            env_path = new File(".");
        }
    /**
     * @param args
     * @throws IOException
     */

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) 
        {
            try
            {
                ArgumentParser parser = ArgumentParsers.newArgumentParser("ErrorCorrection.jar")
                .description("k-mer Error Correction algorithm input arguments parser.");
                
                int dominparamgen = 25;
                int dominparampostpr = 25; //30
                int k = 25;
                int nIter = 3;
                int errorsseglen = 0;
                File fl = null;
                File fl_ref = null;
                int toFindHapl = 1;
                int nProc = Runtime.getRuntime().availableProcessors();
                String alignmethod = ""; // "Muscle" or "Clustal"
                int lt = 50;
                String outFolder = "";
                
                MutuallyExclusiveGroup group = parser.addMutuallyExclusiveGroup("Align");

                group.addArgument("-cl","--clustalw").dest(CLUSTAL_PARAMETER)
                        .action(Arguments.storeConst())
                        .metavar("")
                        .setConst(CLUSATL)
                        .setDefault("")
                        .type(String.class)
                        .help("Enable using of CLustalW for multiple sequence "
                        + "alignment for additional correction procedure (Default: "
                        + "do not use)");
                group.addArgument("-mus","--muscle").dest(MUSCLE_PARAMETER)
                        .action(Arguments.storeConst())
                        .metavar("")
                        .setConst(MUSCLE)
                        .setDefault("")
                        .type(String.class)
                        .help("Enable using of Muscle for multiple sequence "
                        + "alignment for additional correction procedure (Default: "
                        + "do not use)");

                parser.addArgument("-in").dest(READS_PARAMETER)
                        .metavar("Reads File")
                        .help("Folder with files containing reads to be corrected "
                        + " files with extension .fasta (.fas) "
                        + "reads in fasta format")
                        .type(File.class);
                
                parser.addArgument("-out").dest(OUT_PARAMETER)
                        .metavar("Output folder")
                        .help("Folder for output files " +
                                "default: the same as input folder")
                        .setDefault("")
                        .type(String.class);
                
                parser.addArgument("-refs").dest(REFERENCE_PARAMETER)
                        .metavar("Reference File")
                        .help("File containing references "
                        + " file with extension .fasta (.fas) "
                        + "references in fasta format")
                        .type(File.class);

                parser.addArgument("-k").dest(K_PARAMETER)
                        .metavar("Size of k-mer")
                        .setDefault(k)
                        .type(Integer.class)
                        .help("Parameter k - the size of k-mers "
                        + "Default: " + k + ")");
                
                parser.addArgument("-nproc").dest(NPROC_PARAMETER)
                        .metavar("Number of used cores")
                        .setDefault(nProc)
                        .type(Integer.class)
                        .help("Parameter proc - the number of used cores"
                        + "Default: all available");
                
                parser.addArgument("-len").dest(LEN_PARAMETER)
                        .metavar("Minimum reads length")
                        .setDefault(lt)
                        .type(Integer.class)
                        .help("Minimum reads length "
                        + "Default: " + lt + ")");

                parser.addArgument("-i").dest(I_PARAMETER)
                        .metavar("Iterations")
                        .setDefault(nIter)
                        .type(Integer.class)
                        .help("Number of iterations of the algorithm "
                        + "(Default: " + nIter + ")");

                parser.addArgument("-l").dest(L_PARAMETER)
                        .metavar("Num of zeros")
                        .setDefault(errorsseglen)
                        .type(Integer.class)
                        .help("Number of consecutive zeros for finding error threshold. "
                        + "Using Poisson distribution if 0 (Default: " + errorsseglen + ")");
                
                parser.addArgument("-hap").dest(H_PARAMETER)
                        .metavar("output haplotypes")
                        .setDefault(toFindHapl)
                        .type(Integer.class)
                        .help("1, if haplotypes should be outputted "
                        + "0, otherwise (Default: " + toFindHapl + ")");

                parser.addArgument("-dg").dest(DOMINGEN_PARAMETER)
                        .metavar("dominparamgen")
                        .setDefault(dominparamgen)
                        .type(Integer.class)
                        .help("Parameter for haplotypes postprocessing using multiple alignment "
                        + " For positions suspected on homopolymer error if "
                        + "total sum of frequencies with gap on this position "
                        + "is [dominparamgen] times greater than without (or vice versa) "
                        + "then error will be corrected. (Default: "
                        + dominparamgen + ")");

                parser.addArgument("-dpp").dest(DOMINPOSTPROC_PARAMETER)
                        .metavar("dominparampostpr")
                        .setDefault(dominparampostpr)
                        .type(Integer.class)
                        .help("The same as [dominparamgen] but for pairwise "
                        + "postprocessing of haplotypes using "
                        + "alignment of neigbor leaves of neighbor joining tree."
                        + "(Default: " + dominparampostpr + ")");

                try {
/*                    args = new String[6];
                    args[0] = new String("-in");
                    args[1] = new String("G:\\AMD_outbreak\\test\\input");
                    args[2] = new String("-refs");
                    args[3] = new String("G:\\AMD_outbreak\\test\\ref_HVR1.fas");
                    args[4] = new String("-out");
                    args[5] = new String("G:\\AMD_outbreak\\test\\output");
                    for (int i = 0; i < args.length; i++)
                    System.out.println(args[i]);*/
                    
                    
                    
                    Namespace n = parser.parseArgs(args);
                    k = n.getInt(K_PARAMETER);
                    nProc = n.getInt(NPROC_PARAMETER);
                    nIter = n.getInt(I_PARAMETER);
                    String muscle = n.getString(MUSCLE_PARAMETER);
                    String clustalw = n.getString(CLUSTAL_PARAMETER);
                    alignmethod = "".equals(muscle) ? clustalw : muscle;
                    errorsseglen = n.getInt(L_PARAMETER);
                    fl = (File) n.get(READS_PARAMETER);
                    outFolder = n.getString(OUT_PARAMETER);
                    fl_ref = (File) n.get(REFERENCE_PARAMETER);
                    dominparamgen = n.getInt(DOMINGEN_PARAMETER);
                    dominparampostpr = n.getInt(DOMINPOSTPROC_PARAMETER);
                    toFindHapl = n.getInt(H_PARAMETER);
                    lt = n.getInt(LEN_PARAMETER);
                } catch (ArgumentParserException e) {
                    e.printStackTrace();
                    parser.handleError(e);
                    System.exit(1);
                }
                
                
		                
		String folder_name  = fl.getPath();
                if (outFolder.length()  == 0)
                    outFolder = folder_name;
                else
                {
                    File f = new File(outFolder);
                    f.mkdir();
                }
                String refFile_name = fl_ref.getPath();
                
                
                DataSet refs = new DataSet(refFile_name);
                
                int gapop = 15;
                int gapext = 6;
	
                    
		int mErPerc = 50;
		int toClust = 1;
                int minNReads = 10;
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

                    DataSet ds = new DataSet(dset_file);
                    ds.setK(k);
                    ds.setAvProc(nProc);
                    ds.setLenThr(lt);
                    ds.setMaxAllErrorsPerc(mErPerc);
                    ds.setFindErrorsSeglen(errorsseglen);
                    ds.setFileNameShort(dset_file_name);
                    
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
                    cr.setOutfolder(outFolder);
                    cr.run();
                    ds = cr.CorrectedReads();

                    ds.PrintCorrectedReads(outFolder + File.separator +dset_file_name+"_corrected.fas");
                    System.out.println("Finished stage 1!");


                    // SECOND RUN
                    int maxz_postp = 0;
                    int nIter_postp = 0;
                    boolean toPrintStat_postp = false;
                    boolean toDelUncor_postp = true;
                    boolean toPostprocessHeur_postp = false; //???
                    dset_file = outFolder + File.separator +dset_file_name+"_corrected.fas";
                    dset_file_name = dset_file_name+"_corrected.fas";
                    int toFindHapl_postp = 1;
                    boolean toCalcHapl_postp = true;

                    ds = new DataSet(dset_file);
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

                    ds.PrintCorrectedReads(dset_file+"_corrected.fas");
                    if (toFindHapl_postp == 1)
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
            catch(Exception e)
            {
                e.printStackTrace();
                System.exit(1);
            }
	}

}
