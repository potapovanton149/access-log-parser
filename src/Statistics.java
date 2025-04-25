import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private static final Logger log = LoggerFactory.getLogger(Statistics.class);
    private long totalTraffic; //общая сумма трафика данных
    private LocalDateTime minTime; //минимальное время из лога
    private LocalDateTime maxTime; //максимальное время из лога
    private final HashSet<String> sitePages; //список всех страниц из лога
    private final HashMap<String, Integer> infoCountsOS; //статистика по ОС и количеству
    private final HashMap<String, Double> infoStatsOS; //статистика по ОС и значение долей

    public HashMap<String, Integer> getInfoCountsOS() {
        return infoCountsOS;
    }

    public HashMap<String, Double> getInfoStatsOS() {
        return infoStatsOS;
    }

    public HashSet<String> getSitePages() {
        return sitePages;
    }

    public Statistics() {
        this.totalTraffic = 0;
        this.maxTime = LocalDateTime.MIN;
        this.minTime = LocalDateTime.MAX;
        this.sitePages = new HashSet<>();
        this.infoCountsOS = new HashMap<>();
        this.infoStatsOS = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) {
        //считаем объем каждого запроса и суммируем
        totalTraffic += logEntry.getSizeDate();

        //указываем минимальное и максимальное время запроса в логе
        if (logEntry.getDataTime().isBefore(minTime)) {
            minTime = logEntry.getDataTime();
        }
        if (logEntry.getDataTime().isAfter(maxTime)) {
            maxTime = logEntry.getDataTime();
        }

        //добавляем в лист путь запроса если статус 200
        if (logEntry.getHttpCode().equals("200")) {
            sitePages.add(logEntry.getPathRequest());
        }

        //берем из user agent ОС и если значение не пустое +1 к мапе ОС
        String os = logEntry.getUserAgent().getOs();
        if (os.isEmpty()) {
            if (!infoCountsOS.containsKey("empty")) {
                infoCountsOS.put("empty", 1);
            } else {
                infoCountsOS.put("empty", infoCountsOS.get("empty") + 1);
            }
        } else if (!infoCountsOS.containsKey(os)) {
            infoCountsOS.put(os, 1);
        } else {
            infoCountsOS.put(os, infoCountsOS.get(os) + 1);
        }

        //расчет доли от общего значения числа по каждой ОС
        int totalCountOS = 0;
        for (Map.Entry<String, Integer> entry : infoCountsOS.entrySet()) {
            totalCountOS += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : infoCountsOS.entrySet()) {
            double d = (double) entry.getValue() / totalCountOS;
            infoStatsOS.put(entry.getKey(), Math.round(d * 1000.0) / 1000.0);
        }
    }

    //подсчет среднего трафика за час
    public double getTrafficRate() {
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) totalTraffic / hours;
    }
}
