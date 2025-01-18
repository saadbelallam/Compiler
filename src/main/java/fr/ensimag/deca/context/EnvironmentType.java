package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.DecacFatalError;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl24
 * @date 01/01/2025
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
       
        
        envTypes = new HashMap<Symbol, TypeDefinition>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        // envTypes.put(stringSymb, new TypeDefinition(STRING, Location.BUILTIN));
        // not added to envTypes, it's not visible for the user.

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, Location.BUILTIN, null);
        ClassDefinition objectDef = new ClassDefinition(
            OBJECT, 
            Location.BUILTIN,  // Location
            null     // No superclass
        );
        
        // Add built-in methods for Object here if needed
        // For example: equals(), toString(), etc.
        
        // Register Object in the environment
        envTypes.put(objectSymb, objectDef);
        
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public boolean castCompatible(Type sourceType, Type targetType) {
        if (sourceType.equals(targetType)) {
            return true;
        }
       
        if (!sourceType.isInt() && !targetType.isFloat()) {
            return true;
        }
        if (!sourceType.isFloat() && !targetType.isInt()) {
            return true;
        }

        return false;
    }

    public boolean subType(Type t1, Type t2) {
        if (t1 == null || t2 == null) {
            return false;
        }
        if (t1.sameType(t2)) {
            return true;
        }

        // Int is a subtype of float
        if (t1.isInt() && t2.isFloat()) {
            return true;
        }

        // Handle class hierarchy
        if (t1.isClass() && t2.isClass()) {
            ClassDefinition def1 = ((ClassType) t1).getDefinition();
            ClassDefinition def2 = ((ClassType) t2).getDefinition();
            
            while (def1 != null) {
                if (def1 == def2) {
                    return true;
                }
                def1 = def1.getSuperClass();
            }
        }
        
        if (t1.isNull() && t2.isClass()) {
            return true;
        }

        return false;
    }


    
    public boolean assignCompatible(Type t1, Type t2) {
        if (t1.isFloat() && t2.isInt()) {
            return true;
        }
        return subType(t2, t1);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public void declareType(Symbol name, TypeDefinition type) {
        envTypes.put(name, type);
    }

    public Definition get(Symbol name) {
        return envTypes.get(name);
    }

    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final StringType  STRING;
    public final BooleanType BOOLEAN;
    public final NullType NULL;
    public final ClassType OBJECT;
}
