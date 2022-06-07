package report.require;

import database.Login;
import report.util.builder.RequestHeaderBuilder;

import java.lang.ref.ReferenceQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作为AbsHttpRequest的子类，主要作用就是用来解析函数
 */
public class HttpRequest extends AbsHttpRequest {
    protected RequestHeaderBuilder builder;
    private boolean hasLoginParams;

    public HttpRequest(StringBuffer httpContent, ReferenceQueue referenceQueue) {
        super(httpContent, referenceQueue);
        builder = new RequestHeaderBuilder(referenceQueue);
        login = new Login();
    }

    /**
     * Parsing the requisition of the http telegram and create a http telegram in the end of progression.
     *
     * @param httpContent
     */
    @Override
    protected void parseHttpContent(String httpContent) {
        // 这个匹配规则就是：先匹配一个大写字母，然后匹配任意数量的任意字符，最后匹配一个括号中的字符。
        String regex = "([A-Z]{1}.{0,}(\\r\\n){1})";//这里容易出错，测试时使用\\n。实装的时候改为\\r\\n
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(httpContent);
        if (matcher.find()) {
            String group = matcher.group(1);
            String requestURI = parseRequestLine(group);//解析请求行
            String loginParamter = parseFileName(requestURI);
            if (hasLoginParams) parseLoginAttr(loginParamter);
        }
        parseHttpAttribute(matcher);//解析请求属性
        requestHeader = builder.build();
        builder.flush();
    }

    @Override
    protected void parseHttpBody(String httpBody) {

    }

    private String parseRequestLine(String requestLine) {
        int firstIndexOf = requestLine.indexOf(" ");
        int lastIndexOf = requestLine.lastIndexOf(" ");
        String requestURI = requestLine.substring(firstIndexOf + 1, lastIndexOf);//获得请求链接
        String keyWord = requestLine.substring(0, firstIndexOf);
        String httpProtocol = requestLine.substring(lastIndexOf);
        builder.saveRequestLine(keyWord, requestURI, httpProtocol);
        return requestURI;
    }

    private void parseHttpAttribute(Matcher matcher) {
        while (matcher.find()) {
            String group = matcher.group();
            String[] keyAndValue = group.split("(:\\s{1})");
            String key = keyAndValue[0];
            if (keyAndValue.length >= 2) {
                String value = keyAndValue[1];
                builder.parseHttpHead(key, value);
//                setKeyAndValue(key, value);
            } else {
//                setKeyAndValue(key, "\n");
            }
        }
    }

    /**
     * @param requestURI
     * @return
     */
    private String parseFileName(String requestURI) {
        if (requestURI.contains("?")) {
            String[] values = requestURI.split("\\?");
            builder.saveFileName(values[0]);
            hasLoginParams = true;
            return values[1];//return login paramter
        }
        if (requestURI.equals("/")) {
            builder.saveFileName("index.html");
        } else {
            builder.saveFileName(requestURI);
        }
        hasLoginParams = false;
        return null;
    }

    private void parseLoginAttr(String requestURI) {
//        if (requestURI.contains("=")) {
        String[] keyAndValue = requestURI.split("=");
        login.setAccount(keyAndValue[0]);
        if (keyAndValue.length >= 2) {
            login.setPassword(keyAndValue[1]);
            isLogin = login.requireLogin();
            builder.saveFileName(login.getFileName());
//                fileName = login.getFileName();
        }
//        }
    }

    private void requireRegister() {
        String referer = requestHeader.getReferer();
    }


}
