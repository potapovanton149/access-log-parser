import nl.basjes.parse.useragent.UserAgentAnalyzer; // импорт сработал только так. Почему? хз
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgent {
    private static final Logger log = LoggerFactory.getLogger(UserAgent.class);
    private final String userAgentFull; //полная строка user agent
    private final String os; //тип операционной системы
    private final String browser;//тип браузера
    private final boolean signBot;

    //инициализируем анализатор user agent
    private static final UserAgentAnalyzer analyzer =
            UserAgentAnalyzer.newBuilder()
                    .withCache(1000)
                    .hideMatcherLoadStats()
                    .build();

    //конструктор инициализирует объект "пустым" если userAgent в строке отсутствует
    public UserAgent(String userAgentString) {
        this.userAgentFull = userAgentString;
        if ( userAgentString.equals("-")) {
            this.os = "";
            this.browser = "";
            this.signBot = false;
        } else {
            nl.basjes.parse.useragent.UserAgent parsed = analyzer.parse(userAgentString);
            this.browser = parsed.getValue("AgentName");
            this.os = parsed.getValue("OperatingSystemName");
            this.signBot = getUserAgentFull().contains("bot");
        }
    }

    public String getOs() {
        return os;
    }

    public String getUserAgentFull() {
        return userAgentFull;
    }

    public String getBrowser() {
        return browser;
    }

    public boolean getSignBot() {
        return signBot;
    }

    @Override
    public String toString() {
        return userAgentFull;
    }
}