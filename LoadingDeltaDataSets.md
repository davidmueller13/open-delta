To create a DeltaDataSet from text files in directive file format, the argument passed to the DataSetRepository findByName method can be one of two things:
  1. The path to a directory containing the directives files.  In this case, the directory must contain files named specs, items and chars.
  1. The path to a directives file that includes the directives that specify the data set.

```
    String dataSet = "/path/to/";

    DataSetRepository repository = new TextFileDataSetRepository();
    MutableDataSet dataSet = repository.findByName(dataSet, null);

```

For a complete example, see:
http://code.google.com/p/open-delta/source/browse/trunk/common/src/test/java/au/org/ala/delta/model/TextFileDataSetRepositoryTest.java


---


To create a DeltaDataSet from the binary format used by the DELTA Editor:

```

    String dataSet = "/path/to/dataset.dlt";

    DataSetRepository repository = new SlotFileRepository();
    MutableDataSet dataSet = repository.findByName(dataSet, null);

```

In this case the runtime type of the returned `MutableDataSet` will be a `SlotFileDataSet`.