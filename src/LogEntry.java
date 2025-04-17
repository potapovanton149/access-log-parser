import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private static final Logger log = LoggerFactory.getLogger(LogEntry.class);
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

    public LogEntry(String logLine) {
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