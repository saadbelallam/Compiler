package fr.ensimag.deca.tree;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
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
        compiler.addComment("Assignment");

        GPRegister valueReg = compiler.getMangerR().allocateRegister();
        
        try {
          
            getRightOperand().codeGenVal(compiler, valueReg);
            
            AbstractLValue leftOp = getLeftOperand();
            
            if (leftOp instanceof Selection) {

                Selection selection = (Selection) leftOp;
                
       
                GPRegister addrReg = compiler.getMangerR().allocateRegister();
                try {

                    selection.codeGenAddr(compiler, addrReg);
                    
                    
                    compiler.addInstruction(new STORE(valueReg, 
                        new RegisterOffset(selection.getFeildSelc().getFieldDefinition().getIndex(), 
                                         addrReg)));
                    compiler.getMangerR().addNbrStores();
                    
                } finally {
                    compiler.getMangerR().freeRegister(addrReg);
                }
            } else if (leftOp instanceof Identifier) {
                
                Identifier ident = (Identifier) leftOp;
                
                if (ident.getDefinition().isField()) {
                 
                    GPRegister thisReg = compiler.getMangerR().allocateRegister();
                    try {
                        // Load this pointer
                        compiler.addInstruction(new LOAD(
                            new RegisterOffset(-2, Register.LB), thisReg));
                        
                        // Store value in field
                        compiler.addInstruction(new STORE(valueReg, 
                            new RegisterOffset(ident.getFieldDefinition().getIndex(), thisReg)));
                        compiler.getMangerR().addNbrStores();
                        
                    } finally {
                        compiler.getMangerR().freeRegister(thisReg);
                    }
                } else {
                    // Local variable or parameter
                    DAddr addr = ident.getExpDefinition().getOperand();
                    compiler.addInstruction(new STORE(valueReg, addr));
                    compiler.getMangerR().addNbrStores();
                }
            }
            
        } finally {
            compiler.getMangerR().freeRegister(valueReg);
        }
    }
   
       
    /* @Override
    protected void codeGenInst(DecacCompiler compiler) {
    compiler.addComment("Assignment");
    
    // Register for the value to be assigned
    GPRegister valueReg = compiler.getMangerR().allocateRegister();
    getRightOperand().codeGenVal(compiler, valueReg);
    
    try {
        if (getLeftOperand() instanceof Selection) {
            Selection selection = (Selection) getLeftOperand();
            
            // Get address of the object containing the field
            GPRegister objReg = compiler.getMangerR().allocateRegister();
            try {
                selection.codeGenAddr(compiler, objReg);
                
                // Store the value in the field using the correct offset
                DeclField f = (DeclField)field;
                compiler.addInstruction(new STORE(valueReg, 
                    new RegisterOffset(selection.getFeildSelc().get, 
                                     objReg)));
            } finally {
                compiler.getMangerR().freeRegister(objReg);
            }
        } else if (getLeftOperand() instanceof AbstractIdentifier) {
            // Simple variable assignment
            AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
            DAddr addr = ident.getExpDefinition().getOperand();
            compiler.addInstruction(new STORE(valueReg, addr));
        }
    } finally {
        compiler.getMangerR().freeRegister(valueReg);
    }
}
         */

    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister dest) {
        // First generate the assignment
        codeGenInst(compiler);
        
        // Then load the assigned value into destination register
        getLeftOperand().codeGenVal(compiler, dest);
    }
    /* public void codeGenVal(DecacCompiler compiler, GPRegister dest) {
        GPRegister reg = compiler.getMangerR().allocateRegister();
        try {
            getRightOperand().codeGenVal(compiler, reg);
            
            AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
            DAddr addr = ident.getExpDefinition().getOperand();
            compiler.addInstruction(new STORE(reg, addr));
            compiler.getMangerR().addNbrStores();

            
            compiler.addInstruction(new LOAD(addr, dest));
        } finally {
            compiler.getMangerR().freeRegister(reg);
        }
    } */

    @Override
    public int getMaxExpressionDepth() {
        return getRightOperand().getMaxExpressionDepth() + 1;
    }

   

}
