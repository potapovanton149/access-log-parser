import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgent {
    private static final Logger log = LoggerFactory.getLogger(UserAgent.class);
    private final String userAgentFull;
    private final String os;
    private final String browser;

    private static final UserAgentAnalyzer analyzer =
            UserAgentAnalyzer.newBuilder()
                    .withCache(1000)
                    .hideMatcherLoadStats()
                    .build();

    public UserAgent(String userAgentString) {
        this.userAgentFull = userAgentString;
        if ( userAgentString.equals("-")) {
            this.os = "";
            this.browser = "";
        } else {
            nl.basjes.parse.useragent.UserAgent parsed = analyzer.parse(userAgentString);
            this.browser = parsed.getValue("AgentName");
            this.os = parsed.getValue("OperatingSystemName");
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

    @Override
    public String toString() {
        return userAgentFull;
    }
}