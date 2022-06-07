package report.util.content;

import report.util.builder.RequestHeaderBuilder;

/**
 * This {@code RequestHeader} class is the type of data, which
 * saves the http telegram's head that are received by {@code BrowserServer} class.
 * What's more, this class is also used in the {@code AbsHttpRequest} class and it
 * is created by the {@code RequestHeaderBuilder} class.
 * All goble variables are http telegram head.
 *
 * @see report.response.AbsHttpResponse
 * @see RequestHeaderBuilder#build()
 */
public class RequestHeader {
    private String key;//Http request method
    private String uri;//Http request uri
    private String protocol;//Http protocol and version
    private String fileName;
    private String body;
    private String Expires;
    private String Cookie, Connection, Content_Type, Content_Encoding, Content_Language, Content_Length, Content_Location, Content_Range;
    private String Upgrade_Insecure_Requests, User_Agent;
    private String Referer;
    private String Host;
    private String Accept_Encoding, Accept, Accept_Charset, Accept_Language;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

//    RequestHeader(){}

    public String getExpires() {
        return Expires;
    }

    public void Expires(String expires) {
        Expires = expires;
    }

    public String getCookie() {
        return Cookie;
    }

    public void Cookie(String cookie) {
        Cookie = cookie;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAccept_Encoding() {
        return Accept_Encoding;
    }

    public void Accept_Encoding(String accept_Encoding) {
        Accept_Encoding = accept_Encoding;
    }

    public String getUpgrade_Insecure_Requests() {
        return Upgrade_Insecure_Requests;
    }

    public void Upgrade_Insecure_Requests(String upgrade_Insecure_Requests) {
        Upgrade_Insecure_Requests = upgrade_Insecure_Requests;
    }

    public String getConnection() {
        return Connection;
    }

    public void Connection(String connection) {
        Connection = connection;
    }

    public String getContent_Type() {
        return Content_Type;
    }

    public void Content_Type(String content_Type) {
        Content_Type = content_Type;
    }

    public String getContent_Encoding() {
        return Content_Encoding;
    }

    public void Content_Encoding(String content_Encoding) {
        Content_Encoding = content_Encoding;
    }

    public String getContent_Language() {
        return Content_Language;
    }

    public void Content_Language(String content_Language) {
        Content_Language = content_Language;
    }

    public String getContent_Length() {
        return Content_Length;
    }

    public void Content_Length(String content_Length) {
        Content_Length = content_Length;
    }

    public String getContent_Location() {
        return Content_Location;
    }

    public void Content_Location(String content_Location) {
        Content_Location = content_Location;
    }

    public String getContent_Range() {
        return Content_Range;
    }

    public void Content_Range(String content_Range) {
        Content_Range = content_Range;
    }

    public String getReferer() {
        return Referer;
    }

    public void Referer(String referer) {
        Referer = referer;
    }

    public String getHost() {
        return Host;
    }

    public void Host(String host) {
        Host = host;
    }

    public String getAccept() {
        return Accept;
    }

    public void Accept(String accept) {
        Accept = accept;
    }

    public String getAccept_Charset() {
        return Accept_Charset;
    }

    public void Accept_Charset(String accept_Charset) {
        Accept_Charset = accept_Charset;
    }

    public String getAccept_Language() {
        return Accept_Language;
    }

    public void Accept_Language(String accept_Language) {
        Accept_Language = accept_Language;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "key='" + key + '\'' +
                ", uri='" + uri + '\'' +
                ", protocol='" + protocol + '\'' +
                ", body='" + body + '\'' +
                ", Expires='" + Expires + '\'' +
                ", Cookie='" + Cookie + '\'' +
                ", Connection='" + Connection + '\'' +
                ", Content_Type='" + Content_Type + '\'' +
                ", Content_Encoding='" + Content_Encoding + '\'' +
                ", Content_Language='" + Content_Language + '\'' +
                ", Content_Length='" + Content_Length + '\'' +
                ", Content_Location='" + Content_Location + '\'' +
                ", Content_Range='" + Content_Range + '\'' +
                ", Upgrade_Insecure_Requests='" + Upgrade_Insecure_Requests + '\'' +
                ", User_Agent='" + User_Agent + '\'' +
                ", Referer='" + Referer + '\'' +
                ", Host='" + Host + '\'' +
                ", Accept_Encoding='" + Accept_Encoding + '\'' +
                ", Accept='" + Accept + '\'' +
                ", Accept_Charset='" + Accept_Charset + '\'' +
                ", Accept_Language='" + Accept_Language + '\'' +
                '}';
    }

    /**
     * 想要实现的目的：减少检索操作，提升处理消息的效率。
     * <p>
     * 预计设计的功能为：
     * 设计1
     * 该类中含有几个储存数据的类，称该类为：字母类，每个字母类都代表一个字母，这个字母是Http请求报文中的请求属性的第一个字母，
     * 当解析http请求报文中的头部时，会根据头部的第一个字母来将该头部添加到对应的字母类中去。
     * <p>
     * 设计2
     * 该类继承CharSequence，含有两个全局变量int，一个int用于计算每次添加进来的头部的字符数量，另一个用于标记每次添加进头部前数据的总长度。
     * <p>
     * 设计3
     * 解析http头部的时候，直接获取头部信息，在类中寻找对应的类型，传入。或者使用反射来查找。
     */

    public String getUser_Agent() {
        return User_Agent;
    }

    public void User_Agent(String user_Agent) {
        User_Agent = user_Agent;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
