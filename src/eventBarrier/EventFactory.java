package eventBarrier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;


public class EventFactory implements ThreadFactory {
    
    private static final String FOLDER = "testResources\\";
    private static final String SPLIT_AT = "\",\"";
    private List<Thread> threadList = new ArrayList<Thread>();
    private List<Integer> waveList = new ArrayList<Integer>();
    private List<Integer> waveInterval = new ArrayList<Integer>();

    private long raiseTime = 0;
    private Random rgen = new Random();
    
    public EventFactory(String fileName) {
        readFile(fileName);
    }
    
    private void readFile(String fileName){
        URL url = this.getClass().getResource(FOLDER+fileName);
        File file = new File(url.getPath());
        
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(file));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File Not Found!");
        }
        
        try {
            while ((line = br.readLine())!=null){
                String[] params = line.split(SPLIT_AT); 
                boolean crossRand = false;
                int crossTime = 0;
                if(params[0].equals("raiseTime")) 
                    raiseTime = Integer.parseInt(params[1]);
                else if(params[0].equals("random?")) {
                    if(crossRand = Boolean.parseBoolean(params[1])) {
                        crossTime = rgen.nextInt(Integer.parseInt(params[2]));                       
                    }
                } else if (params[0].equals("waveSize")) {
                    int waveSize = Integer.parseInt(params[1]);
                    waveList.add(waveSize);
                } else if (params[0].equals("waveInterval")) {
                    int waveSize = Integer.parseInt(params[1]);
                    waveInterval.add(waveSize);
                } else if (params[0].equals("minstrel")){
                    Minstrel m = new Minstrel();
                    m.setCrossTime(Integer.parseInt(params[1]));
                    threadList.add(newThread(m));
                }                
            }
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("An IO exeption ocurred while reading a line in the file.");
            
        }
        
    }
    
    @Override
    public Thread newThread (Runnable r) {
        return new Thread(r);
    }

    public List<Thread> getThreadList () {
        return threadList;
    }

    public List<Integer> getWaveList () {
        return waveList;
    }

    public long getRaiseTime () {
        return raiseTime;
    }

    public List<Integer> getWaveInterval () {
        return waveInterval;
    }
}