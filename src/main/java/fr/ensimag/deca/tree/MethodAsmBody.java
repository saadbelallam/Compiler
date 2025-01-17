package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;



public class MethodAsmBody extends AbstractMethodBody {
 
    private final StringLiteral asmCode;
  

    public MethodAsmBody(StringLiteral asmCode) {
        this.asmCode = asmCode;
    }
    
    @Override
    protected  void codeGenBody(DecacCompiler compiler){
        String code = asmCode.getValue();
        code = code.substring(1, code.length() - 1)  
                  .replace("\\\"", "\"")             
                  .replace("\\n", "\n")              
                  .replace("\\t", "\t");             
        
        compiler.addComment("Begin assembly code");
        compiler.add(new InlinePortion(code));
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
        return false;
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmCode.prettyPrint(s, prefix, true);
    }

    
}