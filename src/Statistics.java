import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> sitePages;
    private HashMap<String, Integer> countsOS;
    private HashMap<String, Double> statsOS;


    public HashMap<String, Integer> getCountsOS() {
        return countsOS;
    }

    public HashSet<String> getSitePages() {
        return sitePages;
    }

    public Statistics() {
        this.totalTraffic = 0;
        this.maxTime = LocalDateTime.MIN;
        this.minTime = LocalDateTime.MAX;
        this.sitePages = new HashSet<>();
        this.countsOS = new HashMap<>();
        this.statsOS = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getSizeDate();

        if (logEntry.getDataTime().isBefore(minTime)) {
            minTime = logEntry.getDataTime();
        }
        if (logEntry.getDataTime().isAfter(maxTime)) {
            maxTime = logEntry.getDataTime();
        }

        if (logEntry.getHttpCode().equals("200")) {
            sitePages.add(logEntry.getPathRequest());
        }

        String os = logEntry.getUserAgent().getOs();

        if (!os.isEmpty()) {
            if (!countsOS.containsKey(os)) {
                countsOS.put(os, 1);
            } else {
                countsOS.put(os, countsOS.get(os) + 1);
            }
        }
        int totalCountOS = 0;
        for (Map.Entry<String, Integer> entry : countsOS.entrySet()) {
            totalCountOS += entry.getValue();
        }

        for (Map.Entry<String, Integer> entry : countsOS.entrySet()) {
            double d = (double) entry.getValue() / totalCountOS;
            statsOS.put(entry.getKey(), d);
        }
    }

    public double getTrafficRate() {
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) totalTraffic / hours;
    }

    public HashMap<String, Double> getStatsOS() {
        int totalRequests = 0;
        for (int count : countsOS.values()) {
            totalRequests += count;
        }
        for (String os : countsOS.keySet()) {
            double value= (double) countsOS.get(os) / totalRequests;
            statsOS.put(os, Math.round(value * 1000.0) / 1000.0);
        }
        return statsOS;
    }
}
