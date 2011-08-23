package au.org.ala.delta.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * The resource settings class is used to configure multiple locations in which
 * dataset resources can be found. Similar in concept to the Java classpath.
 * 
 * @author ChrisF
 * 
 */
public class ResourceSettings {

    public static final String RESOURCE_PATH_SEPARATOR = ";";
    protected List<String> _resourcePaths;
    protected String _dataSetPath;

    public ResourceSettings() {
        super();
    }

    /**
     * Set the dataset path. Any relative resource paths are looked up relative
     * to the dataset path.
     * 
     * @param path
     */
    public void setDataSetPath(String path) {
        _dataSetPath = path;
    }

    /**
     * Get the dataset path. Any relative resource paths are looked up relative
     * to the dataset path.
     * 
     * @return
     */
    public String getDataSetPath() {
        return _dataSetPath;
    }

    /**
     * @return the first entry on the resource path as an absolute file path.
     */
    public String getFirstResourcePath() {
        if (_resourcePaths.isEmpty()) {
            return "";
        } else {
            return getResourcePathLocations().get(0);
        }
    }

    /**
     * @return the list of resource path locations as a ';' separated String.
     */
    public String getResourcePath() {
        if (_resourcePaths.isEmpty()) {
            return "";
        }
        StringBuilder path = new StringBuilder();
        path.append(_resourcePaths.get(0));
        for (int i = 1; i < _resourcePaths.size(); i++) {
            path.append(RESOURCE_PATH_SEPARATOR);
            path.append(_resourcePaths.get(i));
        }
        return path.toString();
    }

    /**
     * @return A list of the resource path locations
     */
    public List<String> getResourcePathLocations() {
        List<String> retList = new ArrayList<String>();

        for (String imagePath : _resourcePaths) {
            if (imagePath.startsWith("http") || new File(imagePath).isAbsolute() || StringUtils.isEmpty(_dataSetPath)) {
                retList.add(imagePath);
            } else {
                retList.add(_dataSetPath + File.separator + imagePath);
            }
        }

        return retList;
    }

    /**
     * Set the list of resource path locations as a ';' separated String.
     * 
     * @param resourcePath
     *            a list of resource path locations as a ';' separated String.
     */
    public void setResourcePath(String resourcePath) {
        _resourcePaths = new ArrayList<String>();

        _resourcePaths.addAll(Arrays.asList(resourcePath.split(RESOURCE_PATH_SEPARATOR)));
    }

    /**
     * Set the list of resource path locations from a list
     * 
     * @param resourcePaths
     */
    public void setResourcePaths(List<String> resourcePaths) {
        _resourcePaths = new ArrayList<String>(resourcePaths);
    }

    /**
     * Find a file on the resource path. The individual resource path locations
     * are checked in turn until a file with the specified name is found. The dataset path is also
     * searched if the file cannot be found at any of the resource path locations.
     * 
     * @param fileName
     *            The file name
     * @return A URL for the found file, or null if the file was not found.
     */
    public URL findFileOnResourcePath(String fileName) {
        URL fileLocation = null;
        
        List<String> locationsToSearch = getResourcePathLocations();
        
        //If file cannot be found at any of the resource path locations, also search the
        //dataset path itself.
        locationsToSearch.add(getDataSetPath());
        
        for (String locationToSearch : locationsToSearch) {
            try {
                if (locationToSearch.toLowerCase().startsWith("http")) {
                    fileLocation = new URL(locationToSearch + fileName);

                    // Try opening a stream to the remote file. If no exceptions
                    // are thrown, the file
                    // was successfully found at that location. Unfortunately
                    // there is no better way to
                    // test existence of a remote file.
                    fileLocation.openStream();
                    break;
                } else {
                    File f = new File(locationToSearch + File.separator + fileName);
                    if (f.exists()) {
                        fileLocation = f.toURI().toURL();
                        break;
                    }

                }

            } catch (IOException ioexception) {
                // do nothing, keep searching on image path.
            }
        }

        return fileLocation;
    }

    /**
     * Adds the supplied path to the image path as a path relative to the data
     * set path.
     * 
     * @param selectedFile
     *            a file containing the path to add. If the file is relative it
     *            will be added without modification. Otherwise it will be
     *            turned into a path relative to the data set path and then
     *            added.
     */
    public void addToResourcePath(File selectedFile) {
        String relativePath;
        if (selectedFile.isAbsolute()) {
            File dataSetPath = new File(_dataSetPath);

            File parent = parent(selectedFile, dataSetPath);
            File commonParent = dataSetPath;
            String prefix = "";
            while (!parent.equals(commonParent)) {
                prefix += ".." + File.separatorChar;
                commonParent = commonParent.getParentFile();
                parent = parent(selectedFile, commonParent);
            }
            String filePath = selectedFile.getAbsolutePath();
            String parentPath = parent.getAbsolutePath();

            int relativePathIndex = filePath.indexOf(parentPath) + parentPath.length();
            if (!parentPath.endsWith(File.separator)) {
                relativePathIndex++;
            }
            relativePath = prefix + filePath.substring(relativePathIndex);
        } else {
            relativePath = selectedFile.getPath();
        }

        addToResourcePath(relativePath);
    }

    private void addToResourcePath(String relativePath) {
        if (!_resourcePaths.contains(relativePath)) {
            _resourcePaths.add(relativePath);
        }
    }

    private File parent(File start, File parent) {
        if (start.equals(parent) || start.getParentFile() == null) {
            return start;
        } else {
            return parent(start.getParentFile(), parent);
        }
    }

}