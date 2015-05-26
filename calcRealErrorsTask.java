/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import java.util.concurrent.Callable;

/**
 *
 * @author kki8
 */
public class calcRealErrorsTask implements Callable{
    DataSet ds;
    String cloneFile;
    
    calcRealErrorsTask(DataSet ds, String cloneFile)
    {
        this.ds = ds;
        this.cloneFile = cloneFile;
    }

    public Object call() throws Exception {
        ds.calculateRealErrorsStat(cloneFile, 10, 15, 6.6);
        return 0;
    }
    
}
