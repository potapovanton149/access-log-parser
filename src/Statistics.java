import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    int totalTraffic;
    LocalDateTime minTime;
    LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.maxTime = LocalDateTime.MIN;
        this.minTime = LocalDateTime.now();
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getSizeDate();

        if (logEntry.getDataTime().isAfter((maxTime))) {
            maxTime = logEntry.getDataTime();
        }

        if (logEntry.getDataTime().isAfter((maxTime))) {
            minTime = logEntry.getDataTime();
        }
    }

    public double getTrafficRate() {
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) totalTraffic / hours;
    }
}
