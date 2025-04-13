import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    final private String ipAddress;
    final private String propertyOne;
    final private String propertyTwo;
    final private LocalDateTime dataTime;
    final private HttpMethod method;
    final private String pathRequest;
    final private String httpVersion;
    final private String httpCode;
    final private int sizeDate;
    final private String pathReferer;
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
        return method.toString();
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

    public String getPathReferer() {
        return pathReferer;
    }

    public String getUserAgent() {
        return userAgent.toString();
    }

    public LogEntry(String logLine) {
        //тяжелая регулярка
        String regex = "^(\\S+) (\\S+) (\\S+) \\[(.*?)\\] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"(.*?)\" \"(.*?)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);

        this.ipAddress = matcher.group(1);  // IP-адрес
        this.propertyOne = matcher.group(2);  // первое свойство
        this.propertyTwo = matcher.group(3);  // второе свойство

        //какая то непонятная дичь с переводом времени
        String date = matcher.group(4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(date, formatter);
        this.dataTime = zonedDateTime.toLocalDateTime();  // дата время

        this.method = HttpMethod.fromString(matcher.group(5));  // метод запроса
        this.pathRequest = matcher.group(6);  // путь запроса
        this.httpVersion = matcher.group(7);  // версия HTTP
        this.httpCode = matcher.group(8);  // код состояния
        this.sizeDate = Integer.parseInt(matcher.group(9));  // размер данных
        this.pathReferer = matcher.group(10); // Referer путь
        this.userAgent = new UserAgent(matcher.group(11)); // User-Agent

    }
}
