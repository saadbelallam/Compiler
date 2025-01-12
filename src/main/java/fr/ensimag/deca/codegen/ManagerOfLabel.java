package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;



public class ManagerOfLabel {
     
    private int currentLabelNumber = 0;
    private final Label overflowLabel = new Label("overflow_error");
    private final Label io_Label = new Label("io_error");
    Label divByZeroError = new Label("div_by_zero_error");
    private boolean branchOverFlow = false;
    private boolean branchIO = false;
    private boolean branchDivByZero = false;

    
    
    /**
     * Creates a new  label for BOV
     * @return OverFlow Label
     */
    public Label getOverflowLabel() {
        branchOverFlow = true;
        return overflowLabel;
    }

    public Label getIOLabel(){
        branchIO = true;
        return io_Label;
    }

    public Label getDivByZeroLabel(){
        branchDivByZero =true;
        return divByZeroError;
    }

    public boolean getBranchOverFlow(){
        return branchOverFlow;
    }

    public boolean getBranchIO(){
        return branchIO;
    }

    public boolean getBranchDivByZero(){
        return branchDivByZero;
    }
 
    /**
     * Creates a new unique label number
     * @return The next available label number
     */
    public int newLabel() {
        return currentLabelNumber++;
    }
    
    /**
     * Resets the label counter
     */
    public void reset() {
        currentLabelNumber = 0;
    }
}