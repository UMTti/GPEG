# GPEG: JPEG-like compressing algorithm 

JPEG based image compression written in Java. My filename extension is .gpeg . The parts are all same as in JPEG except the Huffman coding:

1. Discrete cosine transformation
2. Quantization
3. Huffman coding is done with constructing a trie, not with statistical way as JPEG does. 

Source code is in jpeg/src folder.

#Usage

First clone the repository. At the moment, this program requires that both width % 8 = 0 and height % 8 = 0. I'm working with support for all images.

* Compress .rgb -image
```
java -jar "/home/username/GPEG/jpeg/dist/jpeg.jar" compress image.rgb image.gpeg height width

```
* Show .gpeg image 

```
java -jar "/home/username/GPEG/jpeg/dist/jpeg.jar" show image.gpeg height width
```
* Extract .gpeg to .rgb

```
java -jar "/home/username/GPEG/jpeg/dist/jpeg.jar" extract image.gpeg image.rgb
```



#Compression rate

* Black compressed image is 2% of original image.
* Colorful test image is 36% of original image.
