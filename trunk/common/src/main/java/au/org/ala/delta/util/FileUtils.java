/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *   
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *   
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.util;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static final String BACKUP_FILE_EXTENSION = ".bak";
	
	/**
	 * Checks if a file exists, if so it is backed up using an extension
	 * of ".bak" and the original is deleted.
	 * @param filePath the full path to the file to check and backup.
	 */
	public static void backupAndDelete(String filePath) {
		File directivesFile = new File(filePath);
		if (directivesFile.exists()) {
			File bakFile = new File(directivesFile.getAbsolutePath() + BACKUP_FILE_EXTENSION);
			directivesFile.renameTo(bakFile);
			
			org.apache.commons.io.FileUtils.deleteQuietly(directivesFile);
		}
	}
	
	/**
	 * Checks if a file exists, if so it is backed up using an extension
	 * of ".bak" and the original is deleted.
	 * @param fileName the file name to check (relative).
	 * @param directoryPath the path to the file to check and backup.
	 */
	public static void backupAndDelete(String fileName, String directoryPath) {
		String fullPath = FilenameUtils.concat(directoryPath, fileName);
		backupAndDelete(fullPath);
	}
	
	public static String makeRelativeTo(String path, File file) {
		String relativePath;
		if (file.isAbsolute()) {
            File dataSetPath = new File(path);

            File parent = parent(file, dataSetPath);
            File commonParent = dataSetPath;
            String prefix = "";
            while (!parent.equals(commonParent)) {
                prefix += ".." + File.separatorChar;
                commonParent = commonParent.getParentFile();
                parent = parent(file, commonParent);
            }
            String filePath = file.getAbsolutePath();
            String parentPath = parent.getAbsolutePath();

            int relativePathIndex = filePath.indexOf(parentPath) + parentPath.length();
            if (!parentPath.endsWith(File.separator)) {
                relativePathIndex++;
            }
            relativePath = prefix + filePath.substring(relativePathIndex);
        } else {
            relativePath = file.getPath();
        }
		return relativePath;

	}
	
	private static File parent(File start, File parent) {
        if (start.equals(parent) || start.getParentFile() == null) {
            return start;
        } else {
            return parent(start.getParentFile(), parent);
        }
    }

	public static File findFileIgnoreCase(String filename) {		
		File f = new File(filename);
		return findFileIgnoreCase(f);
	}
	
	public static File findFileIgnoreCase(File file) {
		
		if (file.exists()) {
			return file;
		}
		
		File parent = file.getParentFile();
		
		if (parent == null) {
			parent = new File(System.getProperty("user.dir"));
		}
		
		if (parent.exists()) {
			String[] matches = parent.list(new CaseInsenstiveFilenameMatcher(file.getName()));
			if (matches.length > 0) {
				String newFilename = String.format("%s%s%s", parent.getAbsolutePath(), File.separator, matches[0]);
				File candidateFile = new File(newFilename);
				if (candidateFile.exists()) {
					return candidateFile;
				}
			}
		}
		
		return null;
	}
	
}

class CaseInsenstiveFilenameMatcher implements FilenameFilter {
	
	private String _name;
	
	public CaseInsenstiveFilenameMatcher(String name) {
		_name = name;
	}

	@Override
	public boolean accept(File dir, String name) {
		if (_name == null) {
			return false;
		}
		
		return _name.equalsIgnoreCase(name);
	}
	
}


