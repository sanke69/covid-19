package fr.main.covid19.data;

import java.util.Collection;

import fr.covid19.data.sources.ecdc.EuropeanCDC;
import fr.covid19.data.sources.ecdc.format.DailyReport;

public class DownloadECDC {

	public static void main(String[] args) {
		System.out.println("Start downloading...");
		Collection<DailyReport> reports = EuropeanCDC.getDailyReports();
		System.out.println("Nb reports= " + reports.size());
	}

}
