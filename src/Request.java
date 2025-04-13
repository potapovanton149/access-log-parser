import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    private String ipAddress;
    private String propertyOne;
    private String propertyTwo;
    private String dataTime;
    private String method;
    private String pathRequest;
    private String httpVersion;
    private String httpCode;
    private String sizeDate;
    private String pathRefere;
    private String userAgent;


    public String getIpAddress() {
        return ipAddress;
    }

    public String getDataTime() {
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

    public String getSizeDate() {
        return sizeDate;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getPathRefere() {
        return pathRefere;
    }

    public String getUserAgent() {
        return userAgent;
    }

    Request(String logLine) {
        //тяжелая регулярка
        String regex = "^([\\d.]+) (\\S+) (\\S+) \\[([^\\]]+)\\] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"([^\"]*)\" \"([^\"]*)\"$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {
            this.ipAddress = matcher.group(1);  // IP-адрес
            this.propertyOne = matcher.group(2);  // первое свойство
            this.propertyTwo = matcher.group(3);  // второе свойство
            this.dataTime = matcher.group(4);  // дата время
            this.method = matcher.group(5);  // метод запроса
            this.pathRequest = matcher.group(6);  // путь запроса
            this.httpVersion = matcher.group(7);  // версия HTTP
            this.httpCode = matcher.group(8);  // код состояния
            this.sizeDate = matcher.group(9);  // размер данных
            this.pathRefere = matcher.group(10); // Referer путь
            this.userAgent = matcher.group(11); // User-Agent
        }

    }
}
