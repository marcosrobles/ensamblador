	ABA
	ADDD	operando
	BCC	operando	
	DBEQ	operando
	LBCC	operando
	ADCA
	M.O.S     
ORG	%00001111 
	M.O.S
Et1	equ	$ffFF 
dos	LDAA	@4732
	SWI
	DES	%0011000011111100
;comentario numero 2 ˇProgramación!
;comentario numero 3
tres	sWi
	End