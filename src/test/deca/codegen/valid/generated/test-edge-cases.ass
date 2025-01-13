; start main program
	TSTO #14	; 0 (temporaires) 14 (variables)
	BOV stack_overflow_error
; Main program
; Variables declarations:
; Variable declaration: a
; Variable declaration: b
; Variable declaration: c
; Variable declaration: d
; Variable declaration: e
; Variable declaration: f
; Variable declaration: g
; Variable declaration: h
; Variable declaration: x
; Variable declaration: result
; Variable declaration: p
; Variable declaration: q
; Variable declaration: r
; Variable declaration: s
; Beginning of main instructions:
; Assignment to a
	LOAD #1, R2
	STORE R2, 1(GB)
; Assignment to b
	LOAD #2, R2
	STORE R2, 2(GB)
; Assignment to c
	LOAD #3, R2
	STORE R2, 3(GB)
; Assignment to d
	LOAD #4, R2
	STORE R2, 4(GB)
; Assignment to e
	LOAD #5, R2
	STORE R2, 5(GB)
; Assignment to f
	LOAD #6, R2
	STORE R2, 6(GB)
; Assignment to g
	LOAD #7, R2
	STORE R2, 7(GB)
; Assignment to h
	LOAD #8, R2
	STORE R2, 8(GB)
; Assignment to result
	LOAD #1, R2
	LOAD 1(GB), R3
	LOAD 2(GB), R4
	CMP R4, R3
	BGE E_Fin_3
	LOAD 3(GB), R4
	LOAD 4(GB), R3
	CMP R3, R4
	BLT E_Fin_2
E_Fin_3:
	LOAD 5(GB), R3
	LOAD 6(GB), R4
	CMP R4, R3
	BGE bool_false_0
	LOAD 7(GB), R4
	LOAD 8(GB), R3
	CMP R3, R4
	BGE bool_false_0
E_Fin_2:
	BRA bool_end_1
bool_false_0:
	LOAD #0, R2
bool_end_1:
	STORE R2, 10(GB)
	WSTR "Complex expression result:"
; Load in R1 to be able to display
	LOAD 10(GB), R1
	CMP #0, R1
	BEQ print_false_5
	WSTR "true"
	BRA print_end_4
print_false_5:
	WSTR "false"
print_end_4:
	WNL
; Assignment to x
	LOAD #10, R2
	STORE R2, 9(GB)
	BRA while_test_6
while_body_7:
; Beginning of if-then-else statement
	LOAD 9(GB), R2
	LOAD #5, R3
	CMP R3, R2
	BLE E_Sinon.9
; Beginning of if-then-else statement
	LOAD 9(GB), R3
	LOAD #7, R2
	CMP R2, R3
	BLE E_Sinon.11
; Beginning of if-then-else statement
	LOAD 9(GB), R2
	LOAD #9, R3
	CMP R3, R2
	BLE E_Sinon.13
	WSTR "Deep nest 1"
	WNL
	BRA E_Fin.14
E_Sinon.13:
	WSTR "Deep nest 2"
	WNL
E_Fin.14:
; End of if-then-else statement
	BRA E_Fin.12
E_Sinon.11:
E_Fin.12:
; End of if-then-else statement
	BRA E_Fin.10
E_Sinon.9:
E_Fin.10:
; End of if-then-else statement
; Assignment to x
	LOAD 9(GB), R3
; allocate R2
	LOAD #1, R2
	SUB R2, R3
; No need for overflow check for type int
; release R2
	STORE R3, 9(GB)
while_test_6:
	LOAD 9(GB), R3
	LOAD #0, R2
	CMP R2, R3
	BLE while_end_8
	BRA while_body_7
while_end_8:
; Assignment to p
	LOAD #1, R2
	STORE R2, 11(GB)
; Assignment to q
	LOAD #0, R2
	STORE R2, 12(GB)
; Assignment to r
	LOAD #1, R2
	STORE R2, 13(GB)
; Assignment to s
	LOAD #0, R2
	STORE R2, 14(GB)
; Assignment to result
	LOAD #1, R2
	LOAD 11(GB), R3
	CMP #0, R3
	BEQ E_Fin_18
	LOAD 12(GB), R3
	CMP #0, R3
	BNE E_Fin_17
E_Fin_18:
	LOAD 13(GB), R3
	CMP #0, R3
	BEQ E_Fin_19
	LOAD 14(GB), R3
	CMP #0, R3
	BEQ E_Fin_17
E_Fin_19:
	LOAD 11(GB), R3
	CMP #0, R3
	BEQ bool_false_15
	LOAD 12(GB), R3
	CMP #0, R3
	BNE bool_false_15
E_Fin_17:
	BRA bool_end_16
bool_false_15:
	LOAD #0, R2
bool_end_16:
	STORE R2, 10(GB)
	WSTR "Chain result:"
; Load in R1 to be able to display
	LOAD 10(GB), R1
	CMP #0, R1
	BEQ print_false_21
	WSTR "true"
	BRA print_end_20
print_false_21:
	WSTR "false"
print_end_20:
	WNL
	HALT
; end main program
stack_overflow_error:
	WSTR "Error: Stack Overflow"
	WNL
	ERROR
