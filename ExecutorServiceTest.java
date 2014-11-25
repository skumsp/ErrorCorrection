/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import com.sun.jmx.snmp.tasks.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceTest {
   private static int NUM_OF_TASKS = 50;

   public ExecutorServiceTest() {}

   public void run() throws InterruptedException, ExecutionException {

      List< Future > futuresList = new ArrayList< Future >();
      int nrOfProcessors = Runtime.getRuntime().availableProcessors();
      ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);

      for(int index = 0; index < NUM_OF_TASKS; index++)
      {
         Read r = new Read("aaa","Read" + index);
         ClustTask ct = new ClustTask(r);
         futuresList.add(eservice.submit(ct));
      }

         Object taskResult;
         for(Future future:futuresList) 
         {
           
               taskResult = future.get();
               System.out.println("result "+taskResult);
         }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
       new ExecutorServiceTest().run();
       System.exit(0);
    }
}