/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ErrorCorrection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
/*import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SubstitutionMatrixHelper;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;*/

/**
 *
 * @author kki8
 */
public class test1 {
public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
        {
    
		File folder = new File("test_genot");
                DataSet ds = new DataSet();
                
                File fl_ref = new File("HCV_HVR1_264_11_temp.fas");
                String refFile_name = fl_ref.getPath();
                DataSet refs = new DataSet(refFile_name,'c');
 
                File[] list_files = folder.listFiles();
                
                for (int i = 0; i < list_files.length; i++)
                {
                    
                    String dset_file = list_files[i].getPath();
                    String dset_file_name = list_files[i].getName();
                    DataSet ds1 = new DataSet(dset_file,"ET");
 //                   ds.reads.addAll(ds1.reads);
                    ds1.fixDirectionGenotypingRefParallel(refs, 20, 6);
                    HashMap<String, DataSet> hm = ds1.separateGenotypes();
                }
 //               ds.PrintUniqueReads("TH208_all");

}
}
