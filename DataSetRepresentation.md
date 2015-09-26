A DELTA data set can be stored in one of three main formats:
  * A set of text files containing DELTA directives. (http://delta-intkey.com/www/uguide.htm#_2._Characters_and)
  * A binary format used by the DELTA editor. (http://delta-intkey.com/www/delta-ed.htm)
  * A binary format used by the Intkey program.  This format contains only a (significant) subset of the data in the other two formats.

The DELTA Editor program is able to convert between the ".dlt" and text/directive file formats.  It is also able to produce a file in the Intkey ".ink" format, however it cannot convert an Intkey file back into one of the other two formats.

Internally, all formats are represented as an instance of a DeltaDataSet.

An example of how to produce a DeltaDataSet from the text file and ".dlt" file formats can be found at [LoadingDeltaDataSets](LoadingDeltaDataSets.md).