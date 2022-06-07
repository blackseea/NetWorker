package report;

import database.Login;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类的主要作用就是用来解析http报文
 */
@Deprecated
public class HTTPRequest {
    private StringBuffer httpContent;
    private ReferenceQueue referenceQueue;
    private StringBuilder header;
    private String requestLine/* 请求行 */, requestURI/* 请求链接 */, httpProtocol/* http协议 */;
    private String keyWord;//请求行中的GET之类的关键字
    private String body;
    private String fileName;
    private Login login;

    public HTTPRequest(StringBuffer httpContent) {
    }

    public HTTPRequest(StringBuffer httpContent, ReferenceQueue referenceQueue) {
        login = new Login();
        this.httpContent = httpContent;
        this.referenceQueue = referenceQueue;
    }

    /**
     * 获取Http报文内容，并且尝试解析http报文
     */
    public void flush() {
        String content = httpContent.toString();
        SoftReference softReference = new SoftReference<>(content, referenceQueue);
//        Objects.requireNonNull(content);
        String[] headerAndBody = content.split("01");
        parseHttpContent(headerAndBody[0]);//解析http报文内容
        if (headerAndBody.length >= 2) {
            body = headerAndBody[1];
        } else body = null;
        content = null;
    }

    /**
     * 解析Http报文内容，并且获取请求属性
     */
    private void parseHttpContent(String httpContent) {
        // 这个匹配规则就是：先匹配一个大写字母，然后匹配任意数量的任意字符，最后匹配一个括号中的字符。
        String regex = "([A-Z]{1}.{0,}(\\r\\n){1})";//这里容易出错，测试时使用\\n。实装的时候改为\\r\\n
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(httpContent);
        if (matcher.find()) {
            parseRequestLine(matcher.group(1));//解析请求行
        }
        parseHttpAttribute(matcher);//解析请求属性
    }

    /**
     * 对requestline进行解析，获取其中的GET、POST关键字，请求链接，还有http协议
     *
     * @param requestLine
     */
    public void parseRequestLine(String requestLine) {
        header = new StringBuilder();
        SoftReference softReference = new SoftReference(header, referenceQueue);
        header.append(requestLine);
//        this.requestLine = requestLine;
        int firstIndexOf = requestLine.indexOf(" ");
        int lastIndexOf = requestLine.lastIndexOf(" ");
        requestURI = requestLine.substring(firstIndexOf + 1, lastIndexOf);
        requestURI = parseFileName(requestURI);
        parseLoginAttr(requestURI);
        keyWord = requestLine.substring(0, firstIndexOf);
        httpProtocol = requestLine.substring(lastIndexOf);
//        header = null;
    }

    /**
     * 获取报文的请求属性
     *
     * @param matcher
     */
    public void parseHttpAttribute(Matcher matcher) {
        while (matcher.find()) {
            String group = matcher.group();
            String[] keyAndValue = group.split("(:\\s{1})");
            String key = keyAndValue[0];
            if (keyAndValue.length >= 2) {
                String value = keyAndValue[1];
                setKeyAndValue(key, value);
            } else {
                setKeyAndValue(key, "\n");
            }
        }
    }

    /**
     * 获取请求链接中的文件名
     *
     * @param requestURI
     * @return
     */
    private String parseFileName(String requestURI) {
        if (requestURI.contains("?")) {
            String[] values = requestURI.split("\\?");
            fileName = values[0];
            return values[1];
        }
        if (requestURI.equals("/")) {
            fileName = "index.html";
        }
        return requestURI;
    }

    /**
     * 如果为登录请求，则解析其中的账号密码
     *
     * @param requestURI
     */
    public void parseLoginAttr(String requestURI) {
//        ([A-Za-z0-9]+\.(ht(?:ml|m)|(ico)))|
//        String regex = "([A-Za-z]{0,}=[A-Za-z0-9]{0,})";
//        Pattern compile = Pattern.compile(regex);
//        Matcher matcher = compile.matcher(requestURI);
//        while (matcher.find()) {
//            String part = matcher.group();
        if (requestURI.contains("=")) {
            String[] keyAndValue = requestURI.split("=");
            login.setAccount(keyAndValue[0]);
            if (keyAndValue.length >= 2) {
                login.setPassword(keyAndValue[1]);
                boolean isLogin = login.requireLogin();
                if (isLogin)
                    fileName = login.getFileName();
                else login.requireRegister();
            }
//            } else {
//                fileName = part;
//            }
        }
    }

    private void setKeyAndValue(String key, String value) {
        header.append(key).append(": ").append(value);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getBody() {
        return body;
    }

    public String getFileName() {
        return fileName;
    }
}
