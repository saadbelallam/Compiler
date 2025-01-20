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
    private int maxReg = 16;
    
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
    
        public boolean getParse() {
            return parse;
        }
    
        public boolean getVerify() {
            return verify;
        }
    
        public static boolean getNoCheck() {
            return noCheck;
        }
            
        public int getMaxReg(){
            return maxReg;
        }

        
        private int debug = 0;
        private boolean parallel = false;
        private boolean printBanner = false;
        private List<File> sourceFiles = new ArrayList<File>();
        private boolean parse = false;
        private boolean verify = false;
        private static boolean noCheck = true;
    
        
        public void parseArgs(String[] args) throws CLIException {
            Logger logger = Logger.getRootLogger();
        
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-b":
                        if (args.length > 1) {
                            throw new CLIException("Option -b incompatible with other options");
                        }
                        printBanner = true;
                        break;
        
                    case "-p":
                        if (this.verify) {
                            throw new CLIException("Option -p incompatible with -v");
                        }
                        this.parse = true;
                        break;
        
                    case "-v":
                        if (this.parse) {
                            throw new CLIException("Option -v incompatible with -p");
                        }
                        this.verify = true;
                        break;
        
                    case "-n":
                        noCheck = false;
                        break;
                    
                        //-r X (registers) : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16
    
                    case "-r":
                        if (args[i].equals("-r")) {
                            if (i + 1 < args.length) {
                                try {
                                    maxReg = Integer.parseInt(args[++i]);
                                if (maxReg < 4 || maxReg > 16) {
                                    throw new CLIException("Invalid value for -r : " + maxReg);
                                }
                            } catch (NumberFormatException e) {
                                throw new CLIException("Invalid value for -r : " + args[i]);
                            }
                        } else {
                            throw new CLIException("Option -r requires a value.");
                        }
                        break;
                        }
    
                    case "-d":
                        debug++;
                        break;
                    
                    case "-P":
                        parallel = true;
                        break;
                    
                    default:
                        // Traite les fichiers source
                        File sourceFile = new File(args[i]);
                        if (!sourceFile.exists() || !sourceFile.isFile()) {
                            throw new CLIException("Invalid source file : " + args[i]);
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
        System.out.println("=========================================================");
        System.out.println("|                      decac                             |");
        System.out.println("=========================================================");
        System.out.println("| Utilisation :                                          |");
        System.out.println("|     decac [options] <fichier_source_1> ...             |");
        System.out.println("|                                                        |");
        System.out.println("| Options disponibles :                                  |");
        System.out.println("|                                                        |");
        System.out.println("|  -b       Affiche une bannière indiquant le nom        |");
        System.out.println("|           de l'équipe.                                 |");
        System.out.println("|                                                        |");
        System.out.println("|  -p       Arrête decac après l'étape de construction   |");
        System.out.println("|           de l'arbre et affiche la décompilation.      |");
        System.out.println("|                                                        |");
        System.out.println("|  -v       Arrête decac après l'étape de vérification.  |");
        System.out.println("|           Ne produit aucune sortie en l'absence        |");
        System.out.println("|           d'erreur.                                    |");
        System.out.println("|                                                        |");
        System.out.println("|  -n       Supprime les tests à l'exécution             |");
        System.out.println("|                                                        |");
        System.out.println("|  -r X     Limite les registres disponibles à R0 ...    |");
        System.out.println("|           R{X-1}, avec 4 <= X <= 16.                   |");
        System.out.println("|                                                        |");
        System.out.println("|  -d       Active les traces de debug. Répéter cette    |");
        System.out.println("|           option pour obtenir plus de traces.          |");
        System.out.println("|                                                        |");
        System.out.println("|  -P       Compile plusieurs fichiers en parallèle.     |");
        System.out.println("|           Permet d'accélérer la compilation.           |");
        System.out.println("|                                                        |");
        System.out.println("=========================================================");
        System.exit(0);
    }

}
