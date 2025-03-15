package picturebot.picture.randomizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PictureRandomizerImplTest {

    @Mock private Random random;

    @InjectMocks private PictureRandomizerImpl pictureRandomizer;

    @Nested
    @DisplayName("getRandomPicture()")
    class GetRandomPicture {

        @Test
        @DisplayName("should return picture")
        void shouldReturnPicture() {
            when(random.nextInt(3)).thenReturn(2);
            final LinkedHashSet<String> pictures = new LinkedHashSet<>(Arrays.asList("dog.jpg", "cat.jpg", "mouse.jpg"));

            // Act:
            final Optional<String> result = pictureRandomizer.getRandomPicture(pictures);

            assertTrue(result.isPresent());
            assertEquals("mouse.jpg", result.get());
        }

        @Test
        @DisplayName("should return empty optional when pictures is null")
        void shouldReturnEmptyOptionalWhenPicturesIsNull() {
            assertTrue(pictureRandomizer.getRandomPicture(Set.of()).isEmpty());
        }    

        @Test
        @DisplayName("should return empty optional when pictures is empty")
        void shouldReturnEmptyOptionalWhenPicturesIsEmpty() {
            assertTrue(pictureRandomizer.getRandomPicture(Set.of()).isEmpty());
        }
    }
}
