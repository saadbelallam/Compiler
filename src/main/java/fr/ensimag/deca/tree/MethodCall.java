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
    private Location location;
     

    public MethodCall(AbstractExpr classM, AbstractIdentifier methodName, ListExpr listExpr ) {
        Validate.notNull(methodName);
        Validate.notNull(listExpr);
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
    public void codeGenVal(DecacCompiler compiler, GPRegister destReg) {
        MethodDefinition methodDef = methodName.getMethodDefinition();
        int paramCount = listExpr.size() + 1;
        compiler.getMangerR().SaveRegIntoStack(compiler);
        int savedReg =  compiler.getMangerR().getnbOfUsedRegisiters();
        compiler.addInstruction(new ADDSP(paramCount +  savedReg + 1));
        GPRegister objReg = compiler.getMangerR().allocateRegister();
        classM.codeGenVal(compiler, objReg);
        compiler.addInstruction(new STORE(objReg, new RegisterOffset(0, Register.SP)));
        compiler.getMangerR().addNbrStores();

        
        int offset = -1;
        for (AbstractExpr arg : listExpr.getList()) {
            arg.codeGenVal(compiler, objReg);
            compiler.addInstruction(new STORE(objReg, new RegisterOffset(offset, Register.SP)));
            compiler.getMangerR().addNbrStores();
            offset--;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), objReg));
        Label nullError = new Label("null_deref");
        compiler.addInstruction(new CMP(new NullOperand(), objReg));
        compiler.addInstruction(new BEQ(nullError));
    
        compiler.addInstruction(new LOAD(new RegisterOffset(0, objReg), objReg));
        int index = methodDef.getOffset();
        /* if(methodName.getName().getName().equals("equals")){
            index = 1;
        } */
        compiler.addInstruction(new BSR(new RegisterOffset(index, objReg)));
        //System.err.println("methoddef="+methodDef.getType() + "index= "+ methodDef.getIndex());
        compiler.addInstruction(new SUBSP(paramCount+savedReg + 1));
        compiler.getMangerR().RestoreSavedRegisters(compiler);
        if (!methodName.getMethodDefinition().getType().isVoid()) {
            compiler.addInstruction(new LOAD(Register.R0, destReg));
        }
        compiler.getMangerR().freeRegister(objReg);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        GPRegister reg = compiler.getMangerR().getScratchRegister(1);
        codeGenVal(compiler, reg);
    }


    @Override
    public void decompile(IndentPrintStream s) {
        if (classM != null){
            classM.decompile(s);
            s.print(".");
        }
        methodName.decompile(s);
        s.print("(");
        listExpr.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        if (classM != null){
            classM.prettyPrint(s, prefix, false);
        }
        methodName.prettyPrint(s, prefix, false);
        listExpr.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        if (classM != null){
            classM.iter(f);
        }
        methodName.iter(f);
        listExpr.iter(f);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("verify MethodCall: start");
        if (classM == null){
            classM = new This();
        }
        Type t = classM.verifyExpr(compiler, localEnv, currentClass);
        if (t == null) {
            throw new ContextualError("Method call '" + methodName.getName() + "' is not associated with any class", methodName.getLocation());
        }
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
        if (listExpr.size() != signature.size()) {
            throw new ContextualError("Expecting " + signature.size() + " parameter(s), but found " + listExpr.size() 
                                    + " parameter(s) in method : " + methodName + ", class : " + classM.getType(), classM.getLocation());
        }
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

     @Override
    public Location getLocation() {
        return location;
    }

   
}

    
