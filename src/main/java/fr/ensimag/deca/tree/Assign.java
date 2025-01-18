package fr.ensimag.deca.tree;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Assign extends AbstractBinaryExpr {
    private static final Logger LOG = Logger.getLogger(AbstractBinaryExpr.class);

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify assign: start");
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setRightOperand(getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
        setType(type);
        LOG.debug("verify assign: end");
        return type;
}



    @Override
    protected String getOperatorName() {
        return "=";
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        GPRegister valueReg = compiler.getMangerR().allocateRegister();
        compiler.addComment("Assignment to field: " + getLeftOperand());
        try {
            // valeur de x means offsetOfXinHeap(valueReg) is the adress of the feild
            getRightOperand().codeGenVal(compiler, valueReg);
           
            
            if (getLeftOperand() instanceof Selection) {
               
                // this.x
                Selection selection = (Selection) getLeftOperand();
                
                
               // value of x is in valueReg
                selection.codeGenStore(compiler, valueReg);
                
            } else if (getLeftOperand() instanceof AbstractIdentifier) {
               
    
                compiler.addComment("Assignment to variable: " + getLeftOperand());
                AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
                DAddr addr = ident.getExpDefinition().getOperand();
                compiler.addInstruction(new STORE(valueReg, addr));
            }
        } finally {
            compiler.getMangerR().freeRegister(valueReg);
        }
    }


    
    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister dest) {
        GPRegister reg = compiler.getMangerR().allocateRegister();
        try {
            getRightOperand().codeGenVal(compiler, reg);
            
            AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
            DAddr addr = ident.getExpDefinition().getOperand();
            compiler.addInstruction(new STORE(reg, addr));
            
            compiler.addInstruction(new LOAD(addr, dest));
        } finally {
            compiler.getMangerR().freeRegister(reg);
        }
    }

    @Override
    public int getMaxExpressionDepth() {
        return getRightOperand().getMaxExpressionDepth() + 1;
    }

   

}
