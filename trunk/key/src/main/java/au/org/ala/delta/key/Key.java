package au.org.ala.delta.key;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;

import au.org.ala.delta.Logger;
import au.org.ala.delta.best.Best;
import au.org.ala.delta.io.BinFileMode;
import au.org.ala.delta.io.BinaryKeyFile;
import au.org.ala.delta.key.directives.KeyDirectiveFileParser;
import au.org.ala.delta.key.directives.io.KeyCharactersFileReader;
import au.org.ala.delta.key.directives.io.KeyItemsFileReader;
import au.org.ala.delta.util.Utils;

public class Key
 {
    
    private KeyContext _context;

    /**
     * @param args
     *            specifies the name of the input file to use.
     */
    public static void main(String[] args) throws Exception {

        StringBuilder credits = new StringBuilder("KEY version 2.12 (Java)");
        credits.append("\n\nM. J. Dallwitz, T.A. Paine");
        credits.append("\n\nCSIRO Division of Entomology, GPO Box 1700, Canberra, ACT 2601, Australia\nPhone +61 2 6246 4075. Fax +61 2 6246 4000. Email delta@ento.csiro.au");
        credits.append("\n\nJava edition ported by the Atlas of Living Australia, 2011.\n");

        System.out.println(credits);

        File f = handleArgs(args);
        if (!f.exists()) {
            Logger.log("File %s does not exist!", f.getName());
            return;
        }

    }

    private static File handleArgs(String[] args) throws Exception {
        String fileName;
        if (args.length == 0) {
            fileName = askForFileName();
        } else {
            fileName = args[0];
        }

        return new File(fileName);
    }

    private static String askForFileName() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        System.out.print("Enter the full pathname of the directives file: ");
        String fileName = in.readLine();

        return fileName;
    }

    public void calculateKey(File directivesFile) {
        _context = new KeyContext();
        _context.setDataDirectory(directivesFile.getParentFile());

        try {
            processDirectivesFile(directivesFile, _context);
        } catch (IOException ex) {
            System.out.println("Error parsing directive file");
            ex.printStackTrace();
        }

        File charactersFile = Utils.createFileFromPath(_context.getCharactersFilePath(), _context.getDataDirectory());
        File itemsFile = Utils.createFileFromPath(_context.getItemsFilePath(), _context.getDataDirectory());

        BinaryKeyFile keyCharactersFile = new BinaryKeyFile(charactersFile.getAbsolutePath(), BinFileMode.FM_READONLY);
        BinaryKeyFile keyItemsFile = new BinaryKeyFile(itemsFile.getAbsolutePath(), BinFileMode.FM_READONLY);

        KeyCharactersFileReader keyCharactersFileReader = new KeyCharactersFileReader(_context.getDataSet(), keyCharactersFile);
        keyCharactersFileReader.createCharacters();

        KeyItemsFileReader keyItemsFileReader = new KeyItemsFileReader(_context, _context.getDataSet(), keyItemsFile);
        keyItemsFileReader.readAll();
        
        doCalculateKey();
    }
    
    private void doCalculateKey() {
        LinkedHashMap<au.org.ala.delta.model.Character, Double> bestMap = Best.orderBest(_context.getDataSet(), _context.getIncludedCharacters(), _context.getIncludedItems(), _context.getRBase(), _context.getVaryWt());
    }

    private void processDirectivesFile(File input, KeyContext context) throws IOException {
        KeyDirectiveFileParser parser = KeyDirectiveFileParser.createInstance();
        parser.parse(input, context);
    }
    
    public KeyContext getContext() {
        return _context;
    }

}
