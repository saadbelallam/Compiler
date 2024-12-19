package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();

    
    public void parseArgs(String[] args) throws CLIException {
        Logger logger = Logger.getRootLogger();
    
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-b":
                    printBanner = true;
                    break;
    
                // case "-p":
                //     break;
    
                // case "-v":
                //     break;
    
                // case "-n":
                //     break;
    
                // case "-r":
                //     break;
    
                // case "-d":
                //     break;
    
                // case "-P":
                //     break;
    
                default:
                    // Traite les fichiers source
                    File sourceFile = new File(args[i]);
                    if (!sourceFile.exists() || !sourceFile.isFile()) {
                        throw new CLIException("Fichier source introuvable ou invalide : " + args[i]);
                    }
                    sourceFiles.add(sourceFile);
                    break;
            }
        }

        // Mappe l'option de debug à Log4j
        switch (debug) {
            case QUIET:
                logger.setLevel(Level.OFF);
                break;
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());
    }
    
    protected void displayUsage() {
        // throw new UnsupportedOperationException("not yet implemented");
    }

}
