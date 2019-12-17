package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.JsonParser;
import bgu.spl.mics.LogManager;
import com.sun.org.apache.xalan.internal.res.XSLTErrorResources;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private static class singletonHolder{
	private static Diary diaryInstance = new Diary();}
	private List<Report> reports;
	int total;
	private LogManager logM = LogManager.getInstance();


	/**
	 * constructor
	 */

	private Diary(){
		reports = new LinkedList<Report>();
		total=0;
	}


	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return singletonHolder.diaryInstance;
	}

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd) throws InterruptedException {
		reports.add(reportToAdd);
		logM.log.info("Report was added to diary");
		sleep(50);
		printToFile("diaryTest.json"); // TBD to delete
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		JsonParser json = new JsonParser(filename);
		json.printTofile(reports);
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total;
	}

	public void increment(){
		total++;
	}
}
