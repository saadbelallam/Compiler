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
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;


/**
 * @author gl24
 * @date 01/01/2025
 */
public class Initialization extends AbstractInitialization {
        private static final Logger LOG = Logger.getLogger(AbstractInitialization.class);

    
    private AbstractExpr expression;

    public AbstractExpr getExpression() {
        return expression;
    }

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        // throw new UnsupportedOperationException("not yet implemented");
        LOG.debug("verify initialization: start");
        this.setExpression(expression.verifyRValue(compiler, localEnv, currentClass, t));
        LOG.debug("verify initialization: end");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        // throw new UnsupportedOperationException("Not yet implemented");
        s.print(" = ");
        expression.decompileInst(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
    
    @Override
    public Type getType() {
        return expression.getType();
    }

     @Override
        public void codeGenInit(DecacCompiler compiler, GPRegister dest, RegisterOffset address) {
            if (expression != null) {
                expression.codeGenVal(compiler, dest);
                compiler.addInstruction(new STORE(dest, address));
            }
        }
    

    public void codeGenInitObject(DecacCompiler compiler) {
        // Generate code to evaluate the initialization expression
        // Result will be stored in R0
        expression.codeGenVal(compiler, Register.R0);
    }}
   
