package picturebot.bot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;

@Component
class GetFileFactoryImpl implements GetFileFactory {

    /**
     * Creates a new GetFile instance configured with the provided file ID.
     * @param fileId the file ID
     * @return a new GetFile instance configured with the provided file ID.
     */
    @Override
    public GetFile createGetFileMethod(final String fileId) {
        return GetFile.builder()
                .fileId(fileId)
                .build();
    }
}
