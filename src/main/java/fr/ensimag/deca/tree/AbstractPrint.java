package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl24
 * @date 01/01/2025
 */
public abstract class AbstractPrint extends AbstractInst {
    private static final Logger LOG = Logger.getLogger(AbstractInst.class);

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        LOG.debug("verify print: start");
        for (AbstractExpr a : getArguments().getList()) {
            // Vérification du type de chaque argument
            a.verifyExpr(compiler, localEnv, currentClass);

            
            Type type = a.getType();
            // Accepte int, float, boolean et string
            if (!type.isInt() && !type.isFloat() && !type.isString()) {
                throw new ContextualError("Println can only print int, float or string", a.getLocation());
            }

        }
        LOG.debug("verify print: end");
    }
    

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            if(this.printHex){
                a.codeGenPrintLnx(compiler);
            }
            else{
            a.codeGenPrint(compiler);
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + getSuffix());

        if (getPrintHex()) {
            s.print("x(");
        } else {
            s.print("(");
        }
        
        arguments.decompile(s);
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
