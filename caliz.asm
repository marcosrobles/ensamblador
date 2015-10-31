	ABA
	ADCA    #%10101     ;bien
	ADCA 	#%abc	    ;no pusiste numeros binarios
	ADCA 	#@8	    ;no es octal
	ADCA 	#$p         ;no es hexadecimal
	ADCA 	#a	    ;no es decimal
	ADCA 	%10101	    ;me va a decir que no es immediato
	ADCA 	@012	     ;me va a decir que no es immediato
	ADCA 	$A1E2	     ;me va a decir que no es immediato
	ADCA 	10	     ;me va a decir que no es immediato
		;y generar errores en base a ese modo
	END