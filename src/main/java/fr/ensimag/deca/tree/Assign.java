package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Assign extends AbstractBinaryExpr {

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
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyRValue(compiler, localEnv, currentClass, type);
        
        // Set the definition on the left operand if it's an identifier
        if (getLeftOperand() instanceof AbstractIdentifier) {
            AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
            ident.setDefinition(localEnv.get(ident.getName()));
        }
        
        setType(type);
        return type;
}



    @Override
    protected String getOperatorName() {
        return "=";
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Generate right operand value first
        GPRegister reg = compiler.getMangerR().allocateRegister();
        getRightOperand().codeGenVal(compiler, reg);
        
        // For left operand, check if it's an identifier
        if (getLeftOperand() instanceof AbstractIdentifier) {
            //System.err.println("getLeftOperand() is = " + getLeftOperand().getExpDefinition().getOperand());
            AbstractIdentifier ident = (AbstractIdentifier) getLeftOperand();
            DAddr addr = ident.getExpDefinition().getOperand();
            compiler.addInstruction(new STORE(reg, addr));
        } else {
            throw new UnsupportedOperationException("Left operand must be an identifier");
        }
}



    
    @Override
    public void codeGenVal(DecacCompiler compiler, GPRegister dest) {
        // 1. Get memory location of left operand (variable)
        System.err.println("Error1");
        RegisterOffset varAddr = getLeftOperand().getMemoryLocation() ;
        System.err.println("Error2");
        
        // 2. Generate code for right operand into dest register
        getRightOperand().codeGenVal(compiler, dest);
        System.err.println("Error3");
         
        // 3. Store result in variable's memory location
        compiler.addInstruction(new STORE(dest, varAddr));
    }

   

}
