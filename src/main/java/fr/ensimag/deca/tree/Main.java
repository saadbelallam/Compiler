package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
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
        LOG.debug("verify Main: start");
        EnvironmentExp env = new EnvironmentExp(null);
        declVariables.verifyListDeclVariable(compiler, env, null);
        insts.verifyListInst(compiler, env, null, compiler.environmentType.VOID);
        LOG.debug("verify Main: end");
    }
    
    // Gestion de l'erreur de débordement de pile
    //compiler.addLabel(new Label("stack_overflow_error"));
    //compiler.addInstruction(new WSTR("Error: Stack Overflow"));
    //compiler.addInstruction(new WNL());
    //compiler.addInstruction(new ERROR());

    //Calcule de la taille de la pile 
    private void StackSize(DecacCompiler compiler) {
        //à compléter
        int variables = declVariables.getNombreVariables();
        int temporaires = compiler.getMangerR().getMaxTemporaires();
        //System.out.print(temporaires);
        int stackSize = variables + temporaires;

        if(variables > 0){
            compiler.addFirst(new ADDSP(variables));
        }
        compiler.addFirst(new BOV(new Label("stack_overflow_error")));
        compiler.addFirst(new TSTO(new ImmediateInteger(stackSize)),temporaires + " (temporaires) " + variables + (" (variables)"));
        compiler.addFirst(null,"start main program");

    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {  
        compiler.addComment("Variables declarations:");
        declVariables.codeGenListVar(compiler);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
        StackSize(compiler);
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
