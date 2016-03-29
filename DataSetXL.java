/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ErrorCorrection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 *
 * @author kki8
 */
public class DataSetXL {
    public LinkedList<Read> reads;
    public HashMap<String,Integer> kmers;
    public int k;
    
    public DataSetXL (String addr, char c) throws IOException
	{
            // keep read name
		reads = new LinkedList<Read>();
		FileReader fr = new FileReader(addr);
		BufferedReader br = new BufferedReader(fr);
		
		String s = br.readLine();
                while(s.equalsIgnoreCase(""))
                    s = br.readLine();
		String nucl = new String();
		String name = new String();
		name = "";
                int count = 1;
		int i = 1;
		int j = s.length();
		while ((i < s.length())&&(s.charAt(i)!=' '))
		{
			name+=s.charAt(i);
			i++;
		}
		s = br.readLine();
		while (s!= null)
		{
                        if (s.length() == 0)
			{
				s = br.readLine();
				continue;
			}
			if(s.charAt(0) == '>')
			{
					reads.add(new Read(nucl, name,1));
                                        System.out.println("Read " + count + " added");
                                        count++;
					nucl = "";
					name = "";
					i = 1;
					while ((i < s.length())&&(s.charAt(i)!=' '))
					{
						name+=s.charAt(i);
						i++;
					}
			}
			else
				nucl+=s.toUpperCase();
			s = br.readLine();
		}
		reads.add(new Read(nucl, name,1));
		fr.close();
	}
    public DataSetXL()
        {
                reads = new LinkedList();
                kmers = new HashMap();

        }
    public void calculateKMersAndKCounts()
    {
        
		kmers = new HashMap<String,Integer>();
//                Iterator ir = reads.iterator();
                int j = 1;
//		while (ir.hasNext())
                for (Read r : reads)
		{
                        System.out.println("Calculate kmers: " + j + "//" + reads.size());
                        j++;
//                        Read r = (Read) ir.next();
			for (int i = 0; i< r.getLength()-k+1; i++)
			{
				String s = r.nucl.substring(i, i+k);
				if (kmers.containsKey(s))
                                {
                                    int f = kmers.get(s);
                                    kmers.put(s, f + r.frequency);
                                }
				else
				{
                                     kmers.put(s, r.frequency);
				}
			}
		}
    }
    public void setK(int i)
    {
	k=i;
    }
    public void PrintReads(String outfile) throws IOException
	{
		FileWriter fw = new FileWriter(outfile);
		for(Read r : reads)
                {
				for (int i = 0; i < r.frequency; i++)
					fw.write(">" + r.name + "_" + i + "\n" + r.getNucl() + "\n");
		}
		fw.close();
	}
    
}
