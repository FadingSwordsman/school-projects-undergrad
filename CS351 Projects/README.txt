Contact Information
	Author: David Lochridge
	Email: locd011@cs.unm.edu
	Date: Thursday, September 9, 2013

-----------------------------------------

Description:
	SiteRiter is a server/parser pair for generating pseudo-randomized websites.
	The specification for the language and feature list supported here are available at:
		http://cs.unm.edu/~ackley/351/projects/1
	alongside the supporting interfaces and  a sample test suite.
	
Specification Issues:
	-> What should be stored as a Symbol is unclear. Additionally, how that Symbol should be stored is unclear.
		It is unclear whether the PROGRAM, CHOICE, etc. Symbols should be stored separately, referenced by parent symbols.
		Because of this, it is also unclear whether symbols such as PROGRAM should be explicity stored, or implicitly stored
		through the use of code instead of data structures containing those symbols.
		I decided to store the PROGRAM symbol implicitly in code, the RULE Symbol explicitly, the HEAD and CHOICE Symbol partially implicitly as a structure in the
		RULE Symbol, and the SEQUENCE symbol explicitly.
	-> The location of EOI tokens, and whether they should be accessible are unclear.
		Since including a symbol for a program would cause redundant storage of Symbols (And violate C.2.3.5)
		in any way I could find to implement a separate Program Symbol, I was limited to storing the EOI token
		on the last rule, or nowhere.  Ultimately, I decided not to store the EOI token.

Implementation Specifics:
	The enclosed SiteRiter is a nearly fully-conforming implementation of the SiteRiter v1.0 specification.
	
	Points of particular design interest include
		-> Lazy lexing, called by the parser only when needed. This goes hand in hand with quick failure.
		-> One-point failure locations, whenever possible. Character interpretation and associated lexing are all handled in one,
			more easily fixed piece of code.

Known Bugs:
	-> Though it isn't specifically mentioned, Tokens used to construct a SEQUENCE Symbol are stored in two places.
		Holding with the spirit of (C.2.3.5), I feel that this is a bug. 

 Acknowledgments:
 	Professor Ackley for creation of the specification, and varied ideas during classes which occurred throughout the creation of the project,
 	and for releasing a test suite that illuminated a number of earlier misunderstandings of the specification.
  	Brady Key for discussing a problem with interpreting integer values from the Reader as chars, thereby allowing this implementation to pass
 	all of the initial aggressive tests provided, and insight during labs into recreating the implementation of the Parser/Lexer combination as
 	a lazy one. 