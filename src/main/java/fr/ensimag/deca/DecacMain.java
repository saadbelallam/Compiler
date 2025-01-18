package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }

        if (options.getPrintBanner()) {
            System.out.println("===============================================");
            System.out.println("|                                             |");
            System.out.println("|      [$]  Projet GL 2025 - ENSIMAG [$]      |");
            System.out.println("|                                             |");
            System.out.println("|    Groupe : GL24                            |");
            System.out.println("|    Membres :                                |");
            System.out.println("|      - BARKATOU Walid                       |");
            System.out.println("|      - BELALLAM Saad                        |");
            System.out.println("|      - BELQASMI Bachir                      |");
            System.out.println("|      - OUAGGASS Yassine                     |");
            System.out.println("|      - SANTOURA Hamza                       |");
            System.out.println("|                                             |");
            System.out.println("===============================================");
            System.exit(0);
        }

        if (options.getSourceFiles().isEmpty()) {
            // throw new UnsupportedOperationException("decac without argument not yet implemented");
            options.displayUsage();
        }
        if (options.getParallel()) {
            int numberOfProcessors = Runtime.getRuntime().availableProcessors();
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfProcessors);
            List<Future<Boolean>> futures = new ArrayList<>();
            try {
                for (File source : options.getSourceFiles()) {
                    futures.add(executorService.submit(() -> {
                        DecacCompiler compiler = new DecacCompiler(options, source);
                        return compiler.compile();
                    }));
                }

                for (Future<Boolean> future : futures) {
                    try {
                        if (future.get()) {
                            error = true;
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                        break;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (error) {
                    throw new IllegalStateException("One or more files failed to compile during parallel execution.");
                }
            } finally {
                executorService.shutdown();
            }

        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
