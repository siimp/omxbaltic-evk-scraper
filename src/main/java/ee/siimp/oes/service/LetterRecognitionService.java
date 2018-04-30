package ee.siimp.oes.service;

import org.bytedeco.javacpp.opencv_core;

public interface LetterRecognitionService {

    String read(opencv_core.Mat mat);
}
