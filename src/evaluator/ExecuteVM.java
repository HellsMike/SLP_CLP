package evaluator;

import parser.SVMParser;

public class ExecuteVM {

    public static final int CODESIZE = 1000 ;
    public static final int MEMSIZE = 1000 ; //10000;

    private AssemblyClass[] code;
    private int[] memory = new int[MEMSIZE];

    private int ip = 0;  			// instruction pointer
    private int sp = MEMSIZE-1 ;	// stack pointer
    private int al = MEMSIZE-2 ; 	// access link
    private int fp = MEMSIZE-1 ; 	// frame pointer
    private int ra;           		// return address
    private int a0;					// the register for values of expressions
    private int t1;					// temporary register
    private int t2;					// additional temporary register, just in case

    public ExecuteVM(AssemblyClass[] _code) {
        code = _code ;
    }


    public void StampaMem(int _j){
        System.out.print(_j + ": " + code[ip].getCode()) ;
        for (int i = MEMSIZE-1; i > sp ; i--){
            System.out.print("\t" + memory[i]) ;
        }
        System.out.println("\t ------" + "SP = " + sp + ", FP = " + fp + ", AL = " + al + ", RA = " + ra + ", A0 = " + a0 + ", T1 = " + t1  ) ;
    }

    public int read(String _strg) {
        return switch (_strg) {
            case "IP" -> ip;
            case "SP" -> sp;
            case "AL" -> al;
            case "FP" -> fp;
            case "RA" -> ra;
            case "A0" -> a0;
            case "T1" -> t1;
            case "T2" -> t2;
            default -> -99999; // error value
        };
    }

    public void update(String _strg, int _val) {
        switch (_strg) {
            case "IP" -> ip = _val;
            case "SP" -> sp = _val;
            case "AL" -> al = _val;
            case "FP" -> fp = _val;
            case "RA" -> ra = _val;
            case "A0" -> a0 = _val;
            case "T1" -> t1 = _val;
            case "T2" -> t2 = _val;
        }
    }

    public void cpu() {
        int j = 0 ;

        while ( true ) {
            StampaMem(j) ;
            j=j+1 ;
            AssemblyClass bytecode = code[ip] ; // fetch
            int tmp ;
            int address;
            switch (bytecode.getCode()) {
                case SVMParser.PUSH -> {
                    push(Integer.parseInt(bytecode.getArg1()));
                    ip = ip + 1;
                }
                case SVMParser.PUSHR -> {
                    push(read(bytecode.getArg1()));
                    ip = ip + 1;
                }
                case SVMParser.POP -> {
                    pop();
                    ip = ip + 1;
                }
                case SVMParser.LOAD -> {
                    tmp = read(bytecode.getArg3()) + Integer.parseInt(bytecode.getArg2());
                    if ((tmp < 0) || (tmp >= MEMSIZE)) {
                        System.out.println("\nError: Null pointer exception");
                        return;
                    } else {
                        memory[tmp] = read(bytecode.getArg1());
                        ip = ip + 1;
                    }
                }
                case SVMParser.STOREI -> {
                    update(bytecode.getArg1(), Integer.parseInt(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.STORE -> {
                    tmp = read(bytecode.getArg3()) + Integer.parseInt(bytecode.getArg2());
                    if ((tmp < 0) || (tmp >= MEMSIZE)) {
                        System.out.println("\nError: Null pointer exception");
                        return;
                    } else {
                        update(bytecode.getArg1(), memory[tmp]);
                        ip = ip + 1;
                    }
                }
                case SVMParser.MOVE -> {
                    update(bytecode.getArg2(), read(bytecode.getArg1()));
                    ip = ip + 1;
                }
                case SVMParser.ADD -> {
                    push(read(bytecode.getArg1()) + read(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.ADDI -> {
                    update(bytecode.getArg1(), read(bytecode.getArg1()) + Integer.parseInt(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.SUB -> {
                    push(read(bytecode.getArg1()) - read(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.SUBI -> {
                    update(bytecode.getArg1(), read(bytecode.getArg1()) - Integer.parseInt(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.MUL -> {
                    push(read(bytecode.getArg1()) * read(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.MULI -> {
                    update(bytecode.getArg1(), read(bytecode.getArg1()) * Integer.parseInt(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.DIV -> {
                    push(read(bytecode.getArg1()) / read(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.DIVI -> {
                    update(bytecode.getArg1(), read(bytecode.getArg1()) / Integer.parseInt(bytecode.getArg2()));
                    ip = ip + 1;
                }
                case SVMParser.POPR -> { //
                    update(bytecode.getArg1(), memory[sp + 1]);
                    pop();
                    ip = ip + 1;
                }
                case SVMParser.BRANCH -> {
                    address = ip + 1;
                    ip = code[address].getCode();
                }
                case SVMParser.BRANCHEQ -> { //
                    if (read(bytecode.getArg1()) == read(bytecode.getArg2())) {
                        address = ip + 1;
                        ip = code[address].getCode();
                    } else ip = ip + 2;
                }
                case SVMParser.BRANCHLESSEQ -> {
                    if (read(bytecode.getArg1()) <= read(bytecode.getArg2())) {
                        address = ip + 1;
                        ip = code[address].getCode();
                    } else ip = ip + 2;
                }
                case SVMParser.JUMPSUB -> {
                    ra = ip + 1;
                    address = ip;
                    ip = Integer.parseInt(code[address].getArg1());
                }
                case SVMParser.RETURNSUB -> ip = read(bytecode.getArg1());
                case SVMParser.HALT -> { //to print the result
                    System.out.println("\nResult: " + a0 + "\n");
                    return;
                }
            }
        }
    }

    private void pop() {
        sp = sp+1 ;
    }

    private void push(int v) {
        memory[sp] = v;
        sp = sp-1 ;
    }

}