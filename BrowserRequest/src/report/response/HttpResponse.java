package report.response;

import report.require.AbsHttpRequest;
import report.util.content.RequestHeader;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.ReferenceQueue;
import java.sql.Ref;
import java.util.Objects;

/**
 * The {@code HttpResponse} class is implementation of {@code AbsHttpResponse} class, which could create a Http Response Telegram.
 */
public class HttpResponse extends AbsHttpResponse {
    private boolean isRequestCookie;

    public HttpResponse(AbsHttpRequest absHttpRequest, ReferenceQueue referenceQueue){
        super(absHttpRequest,referenceQueue);
    }

    public HttpResponse(RequestHeader requestHeader, ReferenceQueue referenceQueue) {
        super(requestHeader, referenceQueue);
    }

    /**
     * Filling the http telegram head according to the file name suffix
     * @param suffix Specific file name's suffix
     * @see #readFile(String)
     */
    @Override
    protected void createResponseTelegram(String suffix) {
        if (suffix == null) {
            header.setHttpLine("HTTP/1.1", "404", "File Not Found");
            return;
        }
        header.setHttpLine("HTTP/1.1", "200", "OK");
        header.setContentType("text/" + suffix + "; charset=utf-8");
//        hheader.setContentLength(5000);
        header.setConnection("Keep-Alive");
    }

    /**
     * Reads the specific file and returns this file name's suffix.
     * @param fileName Needs to read file
     * @return Specific file name's suffix
     */
    @Override
    protected String readFile(String fileName) {
        Objects.requireNonNull(fileName, fileName + " is null");

        File file = new File("res", fileName);
        if (!file.exists()) {
            try {
                throw new FileNotFoundException(fileName + " is not found");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        int indexOf = fileName.lastIndexOf('.');
        String suffix = fileName.substring(indexOf + 1);
        body.readFile2(file);//Implementation method
        return suffix;
    }

    private void register(){

    }


}
