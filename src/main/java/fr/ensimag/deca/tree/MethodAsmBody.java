package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;



public class MethodAsmBody extends AbstractMethodBody {
 
    private final StringLiteral asmCode;
    private boolean hashRet;

  

    public MethodAsmBody(StringLiteral asmCode) {
        this.asmCode = asmCode;
    }
    
    @Override
    protected void codeGenBody(DecacCompiler compiler) {
        // Clean up the assembly code string
        String code = asmCode.getValue();
        code = code.substring(1, code.length() - 1)  
                  .replace("\\\"", "\"")             
                  .replace("\\n", "\n")              
                  .replace("\\t", "\t");             
        
        compiler.addComment("Begin assembly code");
        
        // Add the assembly code
        compiler.add(new InlinePortion(code));
        
        // For methods that return a value, ensure R1 contains the return value
        if (hasReturn()) {
            compiler.addInstruction(new LOAD(Register.R1, Register.R0));
        }
        
        compiler.addComment("End assembly code");
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
       
    }


    @Override
    public void decompile(IndentPrintStream s) {

        s.print(") asm(\"");
        s.print(asmCode.getValue());
        s.println("\");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        asmCode.iter(f);
    }
    @Override
    public  int getLocalVarCount(){
          return 0;
     }

    @Override
    public int getMaxExpressionDepth(){
        return 0;
    }

    @Override
    public boolean hasReturn(){
        return hashRet;
    }

    public void setHasReturnValue(boolean  ret){
        this.hashRet = ret;

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmCode.prettyPrint(s, prefix, true);
    }

    
}