package server.core;

import common.objects.Body;
import common.objects.File;
import common.objects.requests.GetFileRequest;
import server.utils.FileSaver;

import java.sql.SQLException;

public class FileHandler extends AbstractHandler<GetFileRequest> {
    public FileHandler() {
        super(GetFileRequest.class, new String[]{"filePath"}, null);
    }

    @Override
    protected Body onHandle(GetFileRequest body) throws HandleError {
        byte[] bytes = FileSaver.loadAvatar(body.getFilePath());
        File f = new File();
        f.setFile(bytes);
        f.setPath(body.getFilePath());
        return f;
    }
}
