package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl24
 * @date 01/01/2025
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Signature) {
            Signature otherSig = (Signature) other;
            if (args.size() != otherSig.args.size()) {
                return false;
            }
            for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).sameType(otherSig.args.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
