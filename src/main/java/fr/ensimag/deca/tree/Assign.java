package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import net.bytebuddy.implementation.bytecode.member.FieldAccess;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;

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
        // throw new UnsupportedOperationException("not yet implemented");
        Type type = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyRValue(compiler, localEnv, currentClass, type);
        setType(type);
        return type;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        // Gestion des registres
        RegisterManager registerManager = compiler.getRegisterManager();
        GPRegister tempRegister = (GPRegister) registerManager.getRegister();

        try {
            getRightOperand().codeGenExpr(compiler, tempRegister);
        }catch (Exception e)  {
            registerManager.releaseRegister(tempRegister);
            getRightOperand().codeGenInst(compiler);
        }

        try{
        // Cas de AbstractIdentifier
        if (getLeftOperand() instanceof AbstractIdentifier) {
            AbstractIdentifier identifier = (AbstractIdentifier) getLeftOperand();
            VariableDefinition varDef = (VariableDefinition) identifier.getDefinition();
            DAddr operand = varDef.getOperand();

            // On stocke la valeur calculée dans l'adresse mémoire
            compiler.addInstruction(new STORE(tempRegister, operand));
        } else {
            throw new UnsupportedOperationException("Left operand incompatibe");
        }
        System.out.println("Assign");

    } finally {
        // Libération du registre
        registerManager.releaseRegister(tempRegister);
        }
    }

}


