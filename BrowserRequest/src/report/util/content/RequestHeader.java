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
     * ��Ҫʵ�ֵ�Ŀ�ģ����ټ�������������������Ϣ��Ч�ʡ�
     * <p>
     * Ԥ����ƵĹ���Ϊ��
     * ���1
     * �����к��м����������ݵ��࣬�Ƹ���Ϊ����ĸ�࣬ÿ����ĸ�඼����һ����ĸ�������ĸ��Http�������е��������Եĵ�һ����ĸ��
     * ������http�������е�ͷ��ʱ�������ͷ���ĵ�һ����ĸ������ͷ����ӵ���Ӧ����ĸ����ȥ��
     * <p>
     * ���2
     * ����̳�CharSequence����������ȫ�ֱ���int��һ��int���ڼ���ÿ����ӽ�����ͷ�����ַ���������һ�����ڱ��ÿ����ӽ�ͷ��ǰ���ݵ��ܳ��ȡ�
     * <p>
     * ���3
     * ����httpͷ����ʱ��ֱ�ӻ�ȡͷ����Ϣ��������Ѱ�Ҷ�Ӧ�����ͣ����롣����ʹ�÷��������ҡ�
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
