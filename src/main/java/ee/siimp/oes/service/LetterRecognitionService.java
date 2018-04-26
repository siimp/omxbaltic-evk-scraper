package ee.siimp.oes.service;

import org.bytedeco.javacpp.opencv_core;

public interface LetterRecognitionService {

    String readSingleLetter(opencv_core.Mat mat);
}
