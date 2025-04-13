import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    final private String ipAddress;
    final private String propertyOne;
    final private String propertyTwo;
    final private LocalDateTime dataTime;
    final private String method;
    final private String pathRequest;
    final private String httpVersion;
    final private String httpCode;
    final private int sizeDate;
    final private String pathReferes;
    final private UserAgent userAgent;


    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDataTime() {
        return dataTime;
    }

    public String getPropertyTwo() {
        return propertyTwo;
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    public String getMethod() {
        return method;
    }

    public String getPathRequest() {
        return pathRequest;
    }

    public String getHttpCode() {
        return httpCode;
    }

    public int getSizeDate() {
        return sizeDate;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getPathReferes() {
        return pathReferes;
    }

    public String getUserAgent() {
        return userAgent.getUserAgent();
    }

    LogEntry(String logLine) {
        //тяжелая регулярка
        String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([^\\]]+)\\] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"([^\"]*)\" \"([^\"]*)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);

        this.ipAddress = matcher.group(1);  // IP-адрес
        this.propertyOne = matcher.group(2);  // первое свойство
        this.propertyTwo = matcher.group(3);  // второе свойство


        String date = matcher.group(4).substring(1, matcher.group(4).length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z");
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
        this.dataTime = zonedDateTime.toLocalDateTime();  // дата время

        this.method = matcher.group(5);  // метод запроса
        this.pathRequest = matcher.group(6);  // путь запроса
        this.httpVersion = matcher.group(7);  // версия HTTP
        this.httpCode = matcher.group(8);  // код состояния
        this.sizeDate = Integer.parseInt(matcher.group(9));  // размер данных
        this.pathReferes = matcher.group(10); // Referer путь
        this.userAgent = new UserAgent(matcher.group(11)); // User-Agent

    }
}
