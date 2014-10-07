/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author kki8
 */
public class RevCompRun {
    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException
    {
        String refFile_name = "References_306c" + File.separator + "E1E2_454_allgenotypes_forward.fas";
        
        DataSet refs = new DataSet(refFile_name);
        
        String folder_name = "Suma";
        File folder = new File(folder_name);
        System.out.println(folder.exists());
        File[] list_files = folder.listFiles();
        
        int gapop = 15;
        int gapext = 6;

        for (int i = 0; i < list_files.length; i++)
        {
              String dset_file_name = list_files[i].getPath();
              DataSet ds = new DataSet(dset_file_name);
              ds.fixDirectionRefParallel(refs, gapop, gapext);
//              ds.fixDirectionRef(refs, gapop, gapext);
              ds.PrintReads(folder_name + File.separator + list_files[i].getName() + "_reversed.fas");
        }
        System.exit(0);

    }
    
}
