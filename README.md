# omxbaltic-evk-scraper
## Captcha solving with OCR

* OpenCV (javacv) for image manipulation (removing captcha noise)
* Tesseract for character recognition

## General working principle
* use multiple blur + threshold for removing captcha noise
* use cleared image as mask for real captcha
* separate characters using opencv contours
* use ocr or some ML algorithm for character recognition

## TODO
* Grab more sample images
* Make at least 80% of tests pass