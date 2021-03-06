package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;

import api.AbstractEventBarrier;
import eventBarrier.Commuter;

public class EventFactory implements ThreadFactory {

	private static final String FOLDER = "/testFiles/";
	private static final String SPLIT_AT = ",";

	private List<Thread> threadList = new ArrayList<Thread>();
	private List<Integer> waveList = new ArrayList<Integer>();
	private List<Integer> waveInterval = new ArrayList<Integer>();

	private int raiseTime = 0;
	private Random rgen = new Random();
	private AbstractEventBarrier barrier = null;

	public EventFactory(String fileName, AbstractEventBarrier barrier) {
		this.barrier = barrier;
		readFile(fileName);
	}

	private void readFile(String fileName) {
		// URL url = this.getClass().getResourceAsStream(FOLDER+fileName);
		// File file = new File(url.getPath());
		Reader reader = new InputStreamReader(this.getClass().getResourceAsStream(FOLDER + fileName));

		BufferedReader br = null;
		String line = "";

		br = new BufferedReader(reader);
		boolean crossRand = false;
		int crossTimeRange = 0;
		int id = 0;

		try {
			while ((line = br.readLine()) != null) {
				String[] params = line.split(SPLIT_AT);

				if (params[0].startsWith("//"))
					continue;
				else if (params[0].equals("raiseTime"))
					raiseTime = Integer.parseInt(params[1]);
				else if (params[0].equals("random?")) {
					crossRand = Boolean.parseBoolean(params[1]);
					crossTimeRange = Integer.parseInt(params[2]);
				} else if (params[0].equals("waveSize")) {
					int waveSize = Integer.parseInt(params[1]);
					waveList.add(waveSize);
					if (crossRand) {
						for (int i = 0; i < waveSize; i++) {
							Commuter com = new Commuter();
							com.setCrossTime(rgen.nextInt(crossTimeRange));
							com.setBarrier(barrier);
							com.setName("" + id);
							threadList.add(newThread(com));
							id++;
						}
					}
				} else if (params[0].equals("waveInterval")) {
					int waveSize = Integer.parseInt(params[1]);
					waveInterval.add(waveSize);
				} else if (params[0].equals("minstrel")) {
					if (!crossRand) {
						Commuter com = new Commuter();
						com.setCrossTime(Integer.parseInt(params[1]));
						com.setBarrier(barrier);
						com.setName("" + id);
						threadList.add(newThread(com));
						id++;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("An IO exeption ocurred while reading a line in the file.");

		}

	}

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}

	public List<Thread> getThreadList() {
		return threadList;
	}

	public List<Integer> getWaveList() {
		return waveList;
	}

	public int getRaiseTime() {
		return raiseTime;
	}

	public List<Integer> getWaveInterval() {
		return waveInterval;
	}
}
