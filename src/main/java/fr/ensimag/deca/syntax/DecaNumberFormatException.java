package fr.ensimag.deca.syntax;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import java.io.PrintStream;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;


/**
 * Base class for exception to be thrown manually within parser
 * actions.
 * 
 * @date 01/01/2025
 */

class DecaNumberFormatException extends DecaRecognitionException {

    public DecaNumberFormatException(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Number format exception";
    }
}