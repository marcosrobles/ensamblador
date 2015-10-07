	ABA
	ADCA   algo
	M.O.S      ;no debe de decir que requiere operando
ORG	%00001111 ;  no debe de decir que requiere operando
	M.O.S
Et1	equ	$ffFF  ;este no deberia aparecer en el .inst y debe de indicar que no se encontro en tabop
dos	LDAA	@4732
	SWI
	DES	%0011000011111100
;comentario numero 2 ¡Programación!
;comentario numero 3
tres	sWi
	End