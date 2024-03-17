package org.dreds20.httpserver.model;

public class HttpStatus {
    private int code;
    private String message;

    private HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private static HttpStatus from(int code, String message) {
        return new HttpStatus(code, message);
    }

    // 2xx - SUCCESS
    public static HttpStatus OK = from(200, "OK");
    public static HttpStatus CREATED = from(201, "Created");
    public static HttpStatus ACCEPTED = from(202, "Accepted");
    public static HttpStatus NON_AUTHORITATIVE_INFORMATION = from(203, "Non-Authoritative Information");
    public static HttpStatus NO_CONTENT = from(204, "No Content");
    public static HttpStatus RESET_CONTENT = from(205, "Reset Content");
    public static HttpStatus PARTIAL_CONTENT = from(206, "Partial Content");
    public static HttpStatus MULTI_STATUS = from(207, "Multi-Status");
    public static HttpStatus ALREADY_REPORTED = from(208, "Already Reported");
    public static HttpStatus IM_USED = from(226, "IM Used");

    // 3xx - REDIRECTION
    public static HttpStatus MULTIPLE_CHOICES = from(300, "Multiple Choices");
    public static HttpStatus MOVED_PERMANENTLY = from(301, "Moved Permanently");
    public static HttpStatus FOUND = from(302, "Found");
    public static HttpStatus SEE_OTHER = from(303, "See Other");
    public static HttpStatus NOT_MODIFIED = from(304, "Not Modified");
    public static HttpStatus USE_PROXY = from(305, "Use Proxy");
    public static HttpStatus SWITCH_PROXY = from(306, "Switch Proxy");
    public static HttpStatus TEMPORARY_REDIRECT = from(307, "Temporary Redirect");
    public static HttpStatus PERMANENT_REDIRECT = from(307, "Permanent Redirect");

    // 4xx CLIENT ERRORS
    public static HttpStatus BAD_REQUEST = from(400, "Bad Request");
    public static HttpStatus UNAUTHORISED = from(401, "Unauthorised");
    public static HttpStatus PAYMENT_REQUIRED = from(402, "Payment Required");
    public static HttpStatus FORBIDDEN = from(403, "Forbidden");
    public static HttpStatus NOT_FOUND = from(404, "Not Found");
    public static HttpStatus METHOD_NOT_ALLOWED = from(405, "Method Not Allowed");
    public static HttpStatus NOT_ACCEPTABLE = from(406, "Not Acceptable");
    public static HttpStatus PROXY_AUTHENTICATION_REQUIRED = from(407, "Proxy Authentication Required");
    public static HttpStatus REQUEST_TIMEOUT = from(408, "Request Timeout");
    public static HttpStatus CONFLICT = from(409, "Conflict");
    public static HttpStatus GONE = from(410, "Gone");
    public static HttpStatus LENGTH_REQUIRED = from(411, "Length Required");
    public static HttpStatus PRECONDITION_FAILED = from(412, "Precondition Failed");
    public static HttpStatus PAYLOAD_TOO_LARGE = from(413, "Payload Too Large");
    public static HttpStatus URI_TOO_LONG = from(414, "URI Too Long");
    public static HttpStatus UNSUPPORTED_MEDIA_TYPE = from(415, "Unsupported Media Type");
    public static HttpStatus RANGE_NOT_SATISFIABLE = from(416, "Range Not Satisfiable");
    public static HttpStatus EXPECTATION_FAILED = from(417, "Expectation Failed");
    public static HttpStatus IM_A_TEAPOT = from(418, "I'm a teapot"); // why not implement it, nice easter egg
    public static HttpStatus MISDIRECTED_REQUEST = from(421, "Misdirected Request");
    public static HttpStatus UNPROCESSABLE_CONTENT = from(422, "Unprocessable Content");
    public static HttpStatus LOCKED = from(423, "Locked");
    public static HttpStatus FAILED_DEPENDENCY = from(424, "Failed Dependency");
    public static HttpStatus TOO_EARLY = from(425, "Too Early");
    public static HttpStatus UPGRADE_REQUIRED = from(426, "Upgrade Required");
    public static HttpStatus PRECONDITION_REQUIRED = from(428, "Precondition Required");
    public static HttpStatus TOO_MANY_REQUESTS = from(429, "Too Many Requests");
    public static HttpStatus REQUEST_HEADER_FIELDS_TOO_LARGE = from(431, "Request Header Fields Too Large");
    public static HttpStatus UNAVAILABLE_FOR_LEGAL_REASONS = from(451, "Unavailable For Legal Reasons");

    // 5xx SERVER ERRORS
    public static HttpStatus INTERNAL_SERVER_ERROR = from(500, "Internal Server Error");
    public static HttpStatus NOT_IMPLEMENTED = from(501, "Not Implemented");
    public static HttpStatus BAD_GATEWAY = from(502, "Bad Gateway");
    public static HttpStatus SERVICE_UNAVAILABLE = from(503, "Service Unavailable");
    public static HttpStatus GATEWAY_TIMEOUT = from(504, "Gateway Timeout");
    public static HttpStatus HTTP_VERSION_NOT_SUPPORTED = from(505, "HTTP Version Not Supported");
    public static HttpStatus VARIANT_ALSO_NEGOTIATES = from(506, "Variant Also Negotiates");
    public static HttpStatus INSUFFICIENT_STORAGE = from(507, "Insufficient Storage");
    public static HttpStatus LOOP_DETECTED = from(508, "Loop Detected");
    public static HttpStatus NOT_EXTENDED = from(510, "Not Extended");
    public static HttpStatus NETWORK_AUTHENTICATION_REQUIRED = from(511, "Network Authentication Required");

    @Override
    public String toString() {
        return code + " " + message;
    }
}
