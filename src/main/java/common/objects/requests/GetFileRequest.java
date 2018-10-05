package common.objects.requests;

import common.objects.Body;

public class GetFileRequest extends Body {
    public String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
