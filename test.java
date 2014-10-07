/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ErrorCorrection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class test
{
    public static void main(String[] args) throws IOException
    {
         int mb = 1024*1024;
        Runtime rt = Runtime.getRuntime();
        System.out.println(rt.maxMemory()/mb);

		
//                int i = 9;
//                int j = 4;
            String folder_name = "test_autthresh";
            File folder = new File(folder_name);
            System.out.println(folder.exists());
            File[] list_files = folder.listFiles();
        



              for (int i = 0; i < list_files.length; i++)
              {

                int k = 25;
		int lt = 50;
		int mErPerc = 50; // 40
                int errorsseglen = 0;

                 String dset_file_name = list_files[i].getPath();

		String dset_file = dset_file_name;
                
                System.out.println(dset_file_name);

                 DataSet ds = new DataSet(dset_file);


                    ds.setK(k);
                    ds.setFindErrorsSeglen(errorsseglen);
                    ds.setLenThr(lt);
                    ds.setMaxAllErrorsPerc(mErPerc);
                    ds.calculateKMersAndKCounts();
                    ds.findFreqThresholdPoisson();
                    System.out.println(ds.freqThr);
                    System.out.println();
              }
    }
}

