import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    final private String ipAddress; //ip адрес
    final private String propertyOne; //первое свойство
    final private String propertyTwo; //второе свойство
    final private LocalDateTime dataTime; //время и дата
    final private HttpMethod method; //метод http запроса
    final private String pathRequest; // путь запроса
    final private String httpVersion; //версия http
    final private String httpCode; // код состояния http
    final private int sizeDate; //размер данных
    final private String pathReferer; // рефер путь
    final private UserAgent userAgent; // инфо о user agent

    public LogEntry(String logLine) {
        //парсим строку регуляркой
        String regex = "^(?<ip>\\S+) (?<prop1>\\S+) (?<prop2>\\S+) \\[(?<time>[^\\]]+)\\] " +
                "\"(?<method>\\S+) (?<path>[^ ]+) (?<protocol>\\S+)\" " +
                "(?<status>\\d+) (?<size>\\d+) " +
                "\"(?<referer>[^\"]*)\" \"(?<userAgent>[^\"]*)\"";


        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);

        if (!matcher.find()) {
            throw new IllegalArgumentException("ERROR! Неверный формат строки: " + logLine);
        }

        this.ipAddress = matcher.group("ip");
        this.propertyOne = matcher.group("prop1");
        this.propertyTwo = matcher.group("prop2");

        // Парсинг даты и времени
        String date = matcher.group("time");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.dataTime = ZonedDateTime.parse(date, formatter).toLocalDateTime();

        this.method = HttpMethod.fromString(matcher.group("method"));
        this.pathRequest = matcher.group("path");
        this.httpVersion = matcher.group("protocol");
        this.httpCode = matcher.group("status");
        this.sizeDate = Integer.parseInt(matcher.group("size"));
        this.pathReferer = matcher.group("referer");
        this.userAgent = new UserAgent(matcher.group("userAgent"));
    }

    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getDataTime() { return dataTime; }
    public String getPropertyTwo() { return propertyTwo; }
    public String getPropertyOne() { return propertyOne; }
    public String getMethod() { return method.toString(); }
    public String getPathRequest() { return pathRequest; }
    public String getHttpCode() { return httpCode; }
    public int getSizeDate() { return sizeDate; }
    public String getHttpVersion() { return httpVersion; }
    public String getPathReferer() { return pathReferer; }
    public UserAgent getUserAgent() { return userAgent; }
}