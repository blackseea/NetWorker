package report.response;

import report.util.content.ResponseBody;
import report.util.content.RequestHeader;
import report.util.content.ResponseHeader;
import report.require.AbsHttpRequest;

import java.lang.ref.ReferenceQueue;

public abstract class AbsHttpResponse {
    protected ResponseBody body;//
    protected ResponseHeader header;
    protected RequestHeader requestHeader;
    protected ReferenceQueue referenceQueue;
    protected AbsHttpRequest httpRequest;

    protected AbsHttpResponse(AbsHttpRequest httpRequest, ReferenceQueue referenceQueue) {
        this.referenceQueue = referenceQueue;
        this.httpRequest = httpRequest;
    }

    protected AbsHttpResponse(RequestHeader requestHeader,ReferenceQueue referenceQueue)
    {
        this.requestHeader = requestHeader;
        this.referenceQueue = referenceQueue;
    }

    public void flush() {
//        initKeyWord(httpRequest.getKeyWord());
        requestHeader = httpRequest.getRequestHeader();
        initKeyWord(requestHeader.getKey());

    }

    private void initKeyWord(String keyWord) {
        switch (keyWord) {
            case "GET":
//                String requestURI = httpRequest.getRequestURI();
                String requestURI = requestHeader.getUri();
                parseURI(requestURI);
                String suffix = readFile(requestHeader.getFileName());//httpRequest.getFileName()
                createResponseTelegram(suffix);
                break;
            case "POST":
//                String body = httpRequest.getBody();
                throw new RuntimeException();
            default:
                throw new IllegalArgumentException("未查询到关键字");

        }
    }

    private void parseURI(String requestURI) {
        this.header = new ResponseHeader(referenceQueue);
//        this.cookie = new Cookie(referenceQueue);
        this.body = new ResponseBody(referenceQueue);

    }

    public void clean() {
        header.clean();
        body.clean();
    }

    /**
     * 不要调用两次该方法。
     * @return
     */
    @Override
    public String toString() {
        header.setHttpBody(body);
        return header.toString();
    }

    protected abstract void createResponseTelegram(String suffix);

    protected abstract String readFile(String fileName);
}
