package picturebot.bot.factory;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;

@Component
class GetUserProfilePhotosFactoryImpl implements GetUserProfilePhotosFactory {

    @Override
    public GetUserProfilePhotos createGetUserProfilePhotos(final Long userId) {
        return new GetUserProfilePhotos(userId);
    }
}
