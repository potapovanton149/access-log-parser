public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    OPTIONS,
    PATCH,
    TRACE,
    CONNECT,
    UNKNOWN;

    public static HttpMethod fromString(String method) {
       try {
           return HttpMethod.valueOf(method.toUpperCase());
       } catch (IllegalArgumentException e){
           return HttpMethod.UNKNOWN;
       }
    }
}