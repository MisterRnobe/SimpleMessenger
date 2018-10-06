package common.objects.requests;

import common.objects.Body;

public class GetFileRequest extends Body {
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
