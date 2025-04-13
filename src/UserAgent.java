import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    final private String userAgent;
    private String typeOs;
    private String typeBrowser;

    public String getUserAgent() {
        return userAgent;
    }

    public String getTypeOs() {
        return typeOs;
    }

    public String getTypeBrowser() {
        return typeBrowser;
    }

    UserAgent(String userAgent) {
        this.userAgent = userAgent;
        this.typeOs = null;
        this.typeBrowser = null;
        if (!userAgent.equals("-") && !userAgent.contains("compatible")) {
            parsing();
        }
    }

    private void parsing() {
        String result = "";
        String sequence = "OPR";

        int lastIndex = userAgent.lastIndexOf(sequence);
        if (lastIndex != -1) {
            result = userAgent.substring(0, lastIndex + sequence.length());
        }

        String regex = "\\((.*?); (\\\\S+)(?=/[^/]*$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);

        this.typeOs = matcher.group(1);
        this.typeBrowser = matcher.group(2);
    }
}
