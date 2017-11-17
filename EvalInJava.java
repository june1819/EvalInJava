import org.apache.commons.lang3.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Array print function: printArrayString
 *
 * Framework :
 *      -- dataProcess, split numbers and syntax
 *      -- calMain, get brackets first
 *         |-- getBracketIndex
 *         |-- calIndex, calculate equations in brackets
 *             |-- NumOperation, basic numerical operation
 */
public class EvalInJava {
    public static void printArrayString(String[] s){
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.print("\n");
    }

    public static void printArrayString(String str, String[] s){
        System.out.print(str + " ");
        for (int i = 0; i < s.length; i++) {
            System.out.print(s[i] + " ");
        }
        System.out.print("\n");
    }

    public static String dataProcess(String s){
        String finalResult;
        String[] nums;
        String[] operators;

        String operatorReg = "[\\+\\-\\*\\/\\^\\(\\)]";
        String numReg = "\\d+(\\.\\d+)?";

        Pattern pOperator = Pattern.compile(operatorReg);
        Matcher mOperator = pOperator.matcher(s);
        String sOperator = mOperator.replaceAll(" ");
        if(sOperator.charAt(0) != ' '){
            nums = sOperator.split(" ");
        }else{
            nums = sOperator.substring(1).split(" ");
        }

        for (int i = 0; i < nums.length;) {
            if(nums[i].isEmpty()){
                nums = ArrayUtils.remove(nums, i);
            }else{
                i++;
            }
        }
        printArrayString(nums);
        System.out.print("\n");

        Pattern pNums = Pattern.compile(numReg);
        Matcher mNums = pNums.matcher(s);
        String sNums = mNums.replaceAll(" ");
        if(sNums.charAt(0) != ' '){
            operators = sNums.split(" ");
        }else{
            operators = sNums.substring(1).split(" ");
        }

        for (int i = 0; i < operators.length; i++) {
            System.out.print(operators[i] + " ");
        }
        System.out.print("\n");
        finalResult = calMain(operators, nums);
        return finalResult;
    }

    public static int getBracketIndex(String[] o){
        int i;
        for (i = 0; i < o.length; i++) {
            if (o[i].contains(")")) {
                return i;
            }
        }
        return 0;
    }

    public static String calMain(String[] o, String[] n){
        int i;
        String[] result;
        String[] nSub;
        String[] oSub;
        while(ArrayUtils.toString(o).indexOf(")") >= 0) {
            printArrayString("n is", n);
            printArrayString("o is", o);
            System.out.println("length o is " + o.length);

            i = getBracketIndex(o);

            for (int j = i - 1; j >= 0; j--) {
                if (o[j].contains("(")) {
                    System.out.println("i is " + i + ", j is " + j);

                    if (o[j].length() == 1 || o[j].charAt(0) == '(') {
                        nSub = ArrayUtils.subarray(n, j, i);
                    } else {
                        nSub = ArrayUtils.subarray(n, j + 1, i + 1);
                    }

                    oSub = ArrayUtils.subarray(o, j + 1, i);

                    printArrayString("n sub is", nSub);
                    printArrayString("o sub is", oSub);

                    for (int m = 0; m < oSub.length; m++) {
                        if(oSub[m].length() > 1){
                            System.out.println("Syntax error in formula :" + oSub[m]);
                        }
                    }


                    nSub = calIndex(oSub, nSub);

                    if (o[i].length() == 1) {
                        o = ArrayUtils.remove(o, i);
                    } else {
                        o[i] = o[i].substring(1, o[i].length());
                    }

                    if (o[j].length() == 1 || o[j].charAt(0) == '(') {
                        n[j] = nSub[0];
                        for (int k = j + 1; k < i; k++) {
                            n = ArrayUtils.remove(n, j + 1);
                        }

                        for (int k = j + 1; k < i; k++) {
                            o = ArrayUtils.remove(o, j + 1);

                        }
                        if(o[j].length() == 1) {
                            o = ArrayUtils.remove(o, j);
                        }else{
                            o[j] = o[j].substring(0, o[j].length() - 1);
                        }
                    } else {
                        n[j + 1] = nSub[0];

                        for (int k = j + 2; k < i + 1; k++) {
                            n = ArrayUtils.remove(n, j + 2);
                        }

                        for (int k = j + 1; k < i; k++) {
                            o = ArrayUtils.remove(o, j + 1);
                        }

                        o[j] = o[j] = o[j].substring(0, o[j].length() - 1);
                    }
                    break;
                }

            }
        }

        result = calIndex(o, n);
        return result[0];
    }

    public static String[] calIndex(String[] o, String[] n){

        if(o.length == n.length){
            if(o[0].charAt(0) == '-') {
                o = ArrayUtils.remove(o, 0);
                n[0] = '-' + n[0];
            }else if(o[0].charAt(0) == '+'){
                o = ArrayUtils.remove(o, 0);
            }else{
                o = ArrayUtils.remove(o, 0);
                n[0] = "0";
            }
        }

        try {
            for (int i = 0; i < o.length; ) {
                if (o[i].charAt(0) == '^') {
                    n = NumOperation(n, i, i + 1, o[i].charAt(0));
                    o = ArrayUtils.remove(o, i);
                } else {
                    i++;
                }
            }

            for (int i = 0; i < o.length; ) {
                if (o[i].charAt(0) == '*' || o[i].charAt(0) == '/' ) {
                    n = NumOperation(n, i, i + 1, o[i].charAt(0));
                    o = ArrayUtils.remove(o, i);
                } else {
                    i++;
                }
            }

            for (int i = 0; i < o.length; ) {
                if (o[i].charAt(0) == '+' || o[i].charAt(0) == '-') {
                    n = NumOperation(n, i, i + 1, o[i].charAt(0));
                    o = ArrayUtils.remove(o, i);
                } else {
                    i++;
                }
            }
        }catch(NumberFormatException e){
            System.out.println("Error");
            System.exit(0);
        }
        return n;
    }


    public static String[] NumOperation(String[] s, int startPosition, int endPosition, char operator){
        String sRes;
        double res = 0;
        double a = Double.valueOf(s[startPosition]);
        double b = Double.valueOf(s[endPosition]);
        switch (operator){
            case '+': res = a + b;break;
            case '-': res = a - b;break;
            case '^': res = Math.pow(a, b);break;
            case '*': res =a * b;break;
            case '/': if(b != 0){
                res = a / b;
            }
                break;
            default: res = 0;
        }

        String[] sResult = ArrayUtils.remove(s, endPosition);
        if(b != 0){
            sRes = String.format("%.4f", res);
        }else{
            sRes = "Error";
        }
        sResult[startPosition] = sRes;
        System.out.println(sRes);
        System.out.println(" ");
        return sResult;
    }

    public static void main(String[] args) throws IOException{
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String strFormula = "(3+2)";
//        String strFormula = bf.readLine();
        System.out.println(strFormula);

        String r = dataProcess(strFormula);
        System.out.println(r);
    }
}
