package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl24
 * @date 01/01/2025
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // LOG.debug("verify Main: start");
        EnvironmentExp env = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, env, null);
        insts.verifyListInst(compiler, env, null, compiler.environmentType.VOID);
        // LOG.debug("verify Main: end");
    }
    
    //Calcule de la taille de la pile 
    private int getMaxStackSize() {
        //à compléter
        int globalVariables = declVariables.getNombreVariables();
        return globalVariables;
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // A FAIRE: traiter les déclarations de variables.
        System.out.println("début_main");
        compiler.addComment("Beginning of main instructions:");
        //taille de la pile
        // Génération du code pour vérifier la taille de la pile
        int maxStackSize = getMaxStackSize();
        System.out.println("Génération du code pour vérifier la taille de la pile");

        compiler.addInstruction(new TSTO(new ImmediateInteger(maxStackSize)));
        System.out.println("TSTO");
        
        // Initialisation des managers
        compiler.initializeManagers(maxStackSize);
        System.out.println("Initialisation des managers");

        // Ajout de l'espace pour les variables globales
        compiler.addInstruction(new ADDSP(declVariables.getNombreVariables()));
        System.out.println("Ajout de l'espace pour les variables globales");

        // Génération du code pour les variables globals
        for (AbstractDeclVar declVar : declVariables.getList()) {
            System.out.println("codeGenDeclVar");
            declVar.codeGenDeclVar(compiler);
        }   
        //génération de code pour les instructions 
        insts.codeGenListInst(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
