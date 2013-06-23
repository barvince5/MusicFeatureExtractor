MusicFeatureExtractor
=====================

This program offers several functionalities for metadata retrieval and organization, feature extraction and audio file classification, specifically aimed at multimedia files containing music.

In detail, MFE:
 - exploits information stored in the files' ID3 tags to retrieve, from internet, metadata regarding several high and low level information on authors, albums and tracks;
 - obtains system information, such as bitrate or length, about any file;
 - performs low level analysis and extraction of features and statistical data about any file;
 - gives a graphical display of the rhythm histogram of an audio file, in order to allow observation and comparison of the rhythm;
 - uses low level information to derive a rhythm-based classification, while implementing a clustering algorithm (k-means) to group several files in distinct sets.

MFE uses the following libraries:
 - Apache Commons 3 (math 3.2, lang 3.1) - http://commons.apache.org/
 - Entagged 0.35 - http://entagged.sourceforge.net
 - jfreechart 1.0.14 - http://www.jfree.org/jfreechart/
 - jcommon 1.0.17 - http://www.jfree.org/jcommon/
 - jLayer 1.0 - http://www.javazoom.net/javalayer/javalayer.html
 - jSoup 1.7.2 - http://jsoup.org/
 - JMF MP3 Plugin - http://www.oracle.com/technetwork/java/javase/download-137625.html
 - mp3SPI 1.9.4- http://www.javazoom.net/mp3spi/mp3spi.html
 - tritonus share - http://www.tritonus.org/plugins.html

MFE also uses some files taken from Java Audio Feature Extraction developed by Vienna University of Technology (http://www.ifs.tuwien.ac.at/mir/audiofeatureextraction-java/index.html). These files were modified in order to be correctly integrated in MFE, and are located in the package feature.lowLevel.audio. Java Audio Feature Extraction is released under Apache License, Version 2.0.
For more information, see http://www.ifs.tuwien.ac.at/mir/audiofeatureextraction-java/license.html.


