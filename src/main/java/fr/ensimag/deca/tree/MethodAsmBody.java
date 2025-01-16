package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;



public class MethodAsmBody extends AbstractMethodBody {
 
    private final StringLiteral asmCode;
  

    public MethodAsmBody(StringLiteral asmCode) {
        this.asmCode = asmCode;
    }
    
    @Override
    protected  void codeGenBody(DecacCompiler compiler){

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
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        asmCode.prettyPrint(s, prefix, true);
    }

    
}