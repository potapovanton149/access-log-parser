import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Statistics {
    private static final Logger log = LoggerFactory.getLogger(Statistics.class);
    private long totalTraffic; //общая сумма трафика данных
    private LocalDateTime minTime; //минимальное время из лога
    private LocalDateTime maxTime; //максимальное время из лога
    private final HashSet<String> sitePagesSuccess; // список всех страниц из лога с кодом 200
    private final HashSet<String> sitePagesNotFound; // список всех страниц из лога с кодом 404
    private final HashMap<String, Integer> infoCountsOS; //статистика по ОС и количеству
    private final HashMap<String, Double> infoStatsOS; //статистика по ОС и значение долей
    private final HashMap<String, Integer> infoCountsBrowser; //статистика по браузерам и количеству
    private final HashMap<String, Double> infoStatsBrowser; //статистика по браузерам и значение долей
    private int countRequestsBot; //общее количество запросов в логе от ботов
    private int countRequestsGeneral; //общее количество общих запросов
    private int countRequestsBad; // общее количество запросов с кодом состояния 4**
    private int countRequestsServerError; // общее количество запросов с кодом состояния 5**
    private final HashSet<String> uniqueUserIP; //множество уникальных ip адресов пользователей
    private final HashMap<LocalDateTime, Integer> countVisitsPerSecond; //количество посещений сайта в секунду
    private final HashSet<String> infoAllRefer; //все домены из refer
    private final HashMap<String, Integer> countRequestsIP; //количество запросов по каждому ip


    public HashMap<String, Integer> getInfoCountsOS() {
        return infoCountsOS;
    }

    public HashMap<String, Double> getInfoStatsOS() {

        return infoStatsOS;
    }

    public HashSet<String> getSitePagesSuccess() {
        return sitePagesSuccess;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public HashSet<String> getSitePagesNotFound() {
        return sitePagesNotFound;
    }

    public HashMap<String, Integer> getInfoCountsBrowser() {
        return infoCountsBrowser;
    }

    public HashMap<String, Double> getInfoStatsBrowser() {
        return infoStatsBrowser;
    }

    public HashMap<LocalDateTime, Integer> getCountVisitsPerSecond() {
        return countVisitsPerSecond;
    }

    public HashSet<String> getInfoAllRefer() {
        return infoAllRefer;
    }

    public Statistics() {
        this.totalTraffic = 0;
        this.maxTime = LocalDateTime.MIN;
        this.minTime = LocalDateTime.MAX;
        this.sitePagesSuccess = new HashSet<>();
        this.infoCountsOS = new HashMap<>();
        this.infoStatsOS = new HashMap<>();
        this.sitePagesNotFound = new HashSet<>();
        this.infoCountsBrowser = new HashMap<>();
        this.infoStatsBrowser = new HashMap<>();
        this.countRequestsBot = 0;
        this.countRequestsGeneral = 0;
        this.countRequestsBad = 0;
        this.countRequestsServerError = 0;
        this.uniqueUserIP = new HashSet<>();
        this.countVisitsPerSecond = new HashMap<>();
        this.infoAllRefer = new HashSet<>();
        this.countRequestsIP = new HashMap<>();
    }

    public void addEntry(LogEntry logEntry) throws URISyntaxException {
        //считаем количество общих запросов
        countRequestsGeneral++;

        //считаем объем каждого запроса и суммируем
        totalTraffic += logEntry.getSizeDate();

        //считаем общее количество запросов с кодом состояния 4**
        if (logEntry.getHttpCode().matches("^4\\d{2}$")) {
            countRequestsBad++;
        }

        //считаем общее количество запросов с кодом состояния 5**
        if (logEntry.getHttpCode().matches("^5\\d{2}$")) {
            countRequestsServerError++;
        }

        //добавляем ip адреса, но в множество попадут только уникальные
        uniqueUserIP.add(logEntry.getIpAddress());

        //прибавляем счетчик количества запросов ботов если в userAgent есть признак bot
        if (logEntry.getUserAgent().getSignBot()) {
            countRequestsBot++;
        }

        //с помощью класса URI сразу берем домен и добавляем во множество доменов refer
        URI urlRefer = new URI(logEntry.getPathReferer());
        infoAllRefer.add(urlRefer.getHost());

        //указываем минимальное и максимальное время запроса в логе
        if (logEntry.getDataTime().isBefore(minTime)) {
            minTime = logEntry.getDataTime();
        }
        if (logEntry.getDataTime().isAfter(maxTime)) {
            maxTime = logEntry.getDataTime();
        }

        //добавляем путь запроса в множества со статусом 200 или 404
        if (logEntry.getHttpCode().equals("200")) {
            sitePagesSuccess.add(logEntry.getPathRequest());
        }
        if (logEntry.getHttpCode().equals("404")) {
            sitePagesNotFound.add(logEntry.getPathRequest());
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

        //берем из user agent браузер и если значение не пустое +1 к мапе ОС
        String browser = logEntry.getUserAgent().getBrowser();
        if (browser.isEmpty()) {
            if (!infoCountsBrowser.containsKey("empty")) {
                infoCountsBrowser.put("empty", 1);
            } else {
                infoCountsBrowser.put("empty", infoCountsBrowser.get("empty") + 1);
            }
        } else if (!infoCountsBrowser.containsKey(browser)) {
            infoCountsBrowser.put(browser, 1);
        } else {
            infoCountsBrowser.put(browser, infoCountsBrowser.get(browser) + 1);
        }

        //расчет доли от общего значения числа по каждому браузеру
        int totalCountBrowser = 0;
        for (Map.Entry<String, Integer> entry : infoCountsBrowser.entrySet()) {
            totalCountBrowser += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : infoCountsBrowser.entrySet()) {
            double d = (double) entry.getValue() / totalCountBrowser;
            infoStatsBrowser.put(entry.getKey(), d);
        }

        //считаем количество запросов по каждому ip без учета ботов
        String ip = logEntry.getIpAddress();
        if (!ip.isEmpty() && !logEntry.getUserAgent().getSignBot()) {
            if (!countRequestsIP.containsKey(browser)) {
                countRequestsIP.put(ip, 1);
            } else {
                countRequestsIP.put(ip, countRequestsIP.get(ip) + 1);
            }
        }

        //расчет количества запросов в секунду
        if (countVisitsPerSecond.containsKey(logEntry.getDataTime())) {
            countVisitsPerSecond.put(logEntry.getDataTime(), countVisitsPerSecond.get(logEntry.getDataTime()) + 1);
        } else {
            countVisitsPerSecond.put(logEntry.getDataTime(), 1);
        }
    }

    //подсчет среднего трафика за час
    public double getTrafficRate() {
        if (minTime.equals(maxTime)) {
            throw new IllegalArgumentException("ERROR! Максимальное и минимальное время идентичны, невозможно вычислить средний трафик в час.");
        }
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) totalTraffic / hours;
    }

    //подсчет среднего количества посещений в час
    public double getVisitsAverageHour() {
        if (minTime.equals(maxTime)) {
            throw new IllegalArgumentException("ERROR! Максимальное и минимальное время идентичны, невозможно вычислить среднее количество посещений в час.");
        }
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) Math.round((countRequestsGeneral - countRequestsBot) / hours);
    }

    //подсчет среднего количества ошибочных запросов в час
    public double getRequestsFailedAverageHour() {
        if (minTime.equals(maxTime)) {
            throw new IllegalArgumentException("ERROR! Максимальное и минимальное время идентичны, невозможно вычислить среднее количество ошибочных запросов в час.");
        }
        long hours = Duration.between(minTime, maxTime).toHours();
        hours = Math.max(1, hours);
        return (double) Math.round((countRequestsBad + countRequestsServerError) / hours);
    }

    //подсчет средне посещаемости одним пользователем
    public double getAverageUserTraffic() {
        return (double) Math.round((countRequestsGeneral - countRequestsBot) / uniqueUserIP.size());
    }

    //возвращаем ipшник пользака с максимальной посещаемостью
    public String getMaxUserTraffic() {
        Optional<Map.Entry<String, Integer>> maxRequestIP = countRequestsIP.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        return  maxRequestIP.map(Map.Entry::getKey).orElse(null);
    }
}
