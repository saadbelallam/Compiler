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
                    printBanner = true; // Active l'option pour afficher la bannière
                    break;
    
                case "-p":
                    // Active l'option pour s'arrêter après le parsing
                    if (parallel) {
                        throw new CLIException("Les options -p et -P ne peuvent pas être utilisées ensemble.");
                    }
                    break;
    
                case "-v":
                    // Active l'option pour s'arrêter après la vérification
                    if (parallel) {
                        throw new CLIException("Les options -v et -P ne peuvent pas être utilisées ensemble.");
                    }
                    break;
    
                case "-n":
                    // Supprime les vérifications à l'exécution
                    break;
    
                case "-r":
                    // Gère l'option pour limiter les registres
                    if (i + 1 < args.length) {
                        try {
                            int nbRegisters = Integer.parseInt(args[++i]);
                            if (nbRegisters < 4 || nbRegisters > 16) {
                                throw new CLIException("Le nombre de registres doit être entre 4 et 16 inclus.");
                            }
                        } catch (NumberFormatException e) {
                            throw new CLIException("L'option -r doit être suivie d'un nombre entre 4 et 16.");
                        }
                    } else {
                        throw new CLIException("L'option -r doit être suivie d'un argument.");
                    }
                    break;
    
                case "-d":
                    // Active ou augmente le niveau de debug
                    debug++;
                    break;
    
                case "-P":
                    // Active la compilation parallèle
                    if (parallel) {
                        throw new CLIException("L'option -P ne peut être utilisée qu'une seule fois.");
                    }
                    parallel = true;
                    break;
    
                default:
                    if (args[i].startsWith("-")) {
                        throw new CLIException("Option non reconnue : " + args[i]);
                    } else {
                        // Traite les fichiers source
                        File sourceFile = new File(args[i]);
                        if (!sourceFile.exists() || !sourceFile.isFile()) {
                            throw new CLIException("Fichier source introuvable ou invalide : " + args[i]);
                        }
                        sourceFiles.add(sourceFile);
                    }
                    break;
            }
        }
    
        // Vérifie qu'au moins un fichier source a été fourni
        if (sourceFiles.isEmpty() && !printBanner) {
            throw new CLIException("Aucun fichier source spécifié.");
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
        throw new UnsupportedOperationException("not yet implemented");
    }

}
