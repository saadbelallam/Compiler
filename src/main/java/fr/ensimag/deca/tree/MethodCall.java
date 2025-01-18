package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class MethodCall extends AbstractExpr {

    private static final Logger LOG = Logger.getLogger(AbstractExpr.class);


    private AbstractExpr classM;
    final private AbstractIdentifier methodName;
    final private ListExpr listExpr;
     

    public MethodCall(AbstractExpr classM, AbstractIdentifier methodName, ListExpr listExpr ) {
        Validate.notNull(methodName);
        Validate.notNull(listExpr);
        Validate.notNull(classM);
        this.methodName = methodName;
        this.listExpr = listExpr;
        this.classM =classM;
    }

    public MethodCall(AbstractIdentifier methodName, ListExpr listExpr) {
        Validate.notNull(methodName);
        Validate.notNull(listExpr);
        this.methodName = methodName;
        this.listExpr = listExpr;
    }

    @Override 
    public int getMaxExpressionDepth() {
        int maxArgDepth = 0;
        for (AbstractExpr arg : listExpr.getList()) {
            maxArgDepth = Math.max(maxArgDepth, arg.getMaxExpressionDepth());
        }
        return maxArgDepth + listExpr.size() + 3;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
       
        MethodDefinition methodDef = methodName.getMethodDefinition();
        int paramCount = listExpr.size() + 1; 
        GPRegister paramReg = compiler.getMangerR().allocateRegister();
        compiler.addInstruction(new ADDSP(paramCount));
        classM.codeGenVal(compiler, paramReg);
        compiler.addInstruction(new STORE(paramReg, new RegisterOffset(0, Register.SP)));
        
        int offset = -1;
        for (AbstractExpr arg : listExpr.getList()) {
            arg.codeGenVal(compiler, paramReg);
            compiler.addInstruction(new STORE(paramReg, new RegisterOffset(offset, Register.SP)));
            offset--;
        }
    
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), paramReg));
        
        Label nullError = new Label("null_deref");
        compiler.addInstruction(new CMP(new NullOperand(), paramReg));
        compiler.addInstruction(new BEQ(nullError));

        compiler.addInstruction(new LOAD(new RegisterOffset(0, paramReg), paramReg));
        compiler.addInstruction(new BSR(new RegisterOffset(methodDef.getIndex(), paramReg)));
        
        compiler.addInstruction(new SUBSP(paramCount));

        compiler.getMangerR().freeRegister(paramReg);
        

       
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'decompile'");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
        methodName.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, false);
        if (classM != null){
            classM.prettyPrint(s, prefix, false);
        }


    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterChildren'");
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify MethodCall: start");
        classM.verifyExpr(compiler, localEnv, currentClass);
        Type typeClass = classM.getType();

        if (typeClass == null || !typeClass.isClass()) {
            throw new ContextualError("Method call '" + methodName.getName() + "' is not associated with any class", getLocation());
        } 

        

        ClassDefinition classDef = (ClassDefinition) compiler.environmentType.get(typeClass.getName());
        if (classDef == null) {
            throw new ContextualError("Class not found: " + typeClass.getName(), getLocation());
        }

        ExpDefinition def = classDef.getMembers().get(methodName.getName());
        if (def == null || !def.isMethod()) {
            throw new ContextualError("Method '" + methodName.getName() + 
                                    "' not found in class " + classDef.getType().getName(), 
                                    getLocation());
        }
        methodName.verifyExpr(compiler, classDef.getMembers(), currentClass);
        Signature signature = methodName.getMethodDefinition().getSignature();
        int numParams = 0;

        for (AbstractExpr expr : listExpr.getList()) {
            if (numParams < signature.size()) {
                listExpr.set(numParams, expr.verifyRValue(compiler, localEnv, currentClass, signature.paramNumber(numParams)));
                numParams++;
            } else {
                throw new ContextualError("Expecting " + signature.size() + " parameter(s), but found " + listExpr.getList().size() 
                                        + " parameter(s) in method : " + methodName + ", class : " + classM.getType(), getLocation());
            }
        }

        setType(methodName.getType());
        LOG.debug("verify MethodCall: end");
        return methodName.getType();
    }
}

    
