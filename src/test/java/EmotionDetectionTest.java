import OpenCV.EmotionDetection;
import org.junit.jupiter.api.Test;


public class EmotionDetectionTest {

    @Test
    void testEmotion() {
        //Expected emotion
        String expected = "HAPPY";

        //Create instance
        EmotionDetection emotionDetection = new EmotionDetection();

        //Call detect method which detects emotion in a specified image
//        String result = emotionDetection.detect("U:\\Year 4\\SQE\\princeWillHappy.jpg");

//        assertEquals("Expected emotion: " + expected, expected, result);
    }
}
