package arbitraryarithmetic;

public class AFloat {
    private AInteger totalValue;
    private int NumOfDigitsAfterDecimal;
    

    public AFloat() {
        this.totalValue = new AInteger("0");
        this.NumOfDigitsAfterDecimal = 0;
    }

    public AFloat(String s) {
        if (s == null || s.isEmpty())
        throw new IllegalArgumentException("empty string");

        String[] parts = s.split("\\.");

        if(parts.length > 2)
        throw new IllegalArgumentException("Invalid float format");

        if(parts.length == 2){
        this.NumOfDigitsAfterDecimal = parts[1].length();
        this.totalValue = new AInteger(parts[0] + parts[1]);
        }
        else {
            this.NumOfDigitsAfterDecimal = 0;
            this.totalValue = new AInteger(parts[0] + "");
        }
    }   


    public AFloat(AFloat other) {
        this.totalValue = new AInteger(other.totalValue);
        this.NumOfDigitsAfterDecimal = other.NumOfDigitsAfterDecimal;
    }

    public static AFloat parse(String s) {
        return new AFloat(s);
    }
    
    private AInteger scaleup(AInteger num, int byPlaces) {
        if(byPlaces <= 0) return new AInteger(num.toString());

        StringBuilder shiv = new StringBuilder(num.toString());
        
            for(int i = 0; i<byPlaces; i++){
                shiv.append('0');
        }

        return new AInteger(shiv.toString());
    }



    public AFloat add(AFloat other) {
        int maxDecimalDigits = Math.max(this.NumOfDigitsAfterDecimal,other.NumOfDigitsAfterDecimal);

        AInteger add1 = this.totalValue;
        AInteger add2 = other.totalValue;

        if(maxDecimalDigits > this.NumOfDigitsAfterDecimal) {
        add1 = scaleup(this.totalValue, (maxDecimalDigits-this.NumOfDigitsAfterDecimal));
        }
        else if(maxDecimalDigits > other.NumOfDigitsAfterDecimal) {
        add2 = scaleup(other.totalValue, (maxDecimalDigits-other.NumOfDigitsAfterDecimal));
        }

        AInteger resultTotalValue = add1.add(add2);
        AFloat result = new AFloat();
        result.totalValue = resultTotalValue;
        result.NumOfDigitsAfterDecimal = maxDecimalDigits;

        return result;
    }

    public AFloat subtract(AFloat other) {
        int maxDecimalDigits = Math.max(this.NumOfDigitsAfterDecimal,other.NumOfDigitsAfterDecimal);

       AInteger sub1 = this.totalValue;
       AInteger sub2 = other.totalValue;

        if(maxDecimalDigits > this.NumOfDigitsAfterDecimal) {
        sub1 = scaleup(this.totalValue, maxDecimalDigits-this.NumOfDigitsAfterDecimal);
        }
        else if(maxDecimalDigits > other.NumOfDigitsAfterDecimal) {
        sub2 = scaleup(other.totalValue, maxDecimalDigits-other.NumOfDigitsAfterDecimal);
        }

        AInteger resultTotalValue = sub1.subtract(sub2);
        AFloat result = new AFloat();
        result.totalValue = resultTotalValue;
        result.NumOfDigitsAfterDecimal = maxDecimalDigits;

        return result;
    }

    public AFloat multiply(AFloat other) {
        AInteger resultTotalValue = this.totalValue.multiply(other.totalValue);
        int noOfDeci = this.NumOfDigitsAfterDecimal + other.NumOfDigitsAfterDecimal;

        AFloat result = new AFloat();
        result.NumOfDigitsAfterDecimal = noOfDeci;
        result.totalValue = resultTotalValue;

        return result;
    }

    private static String divideStringsFloat(String dividend, String divisor, boolean getDecimals) {
        if(divisor.equals("0")) {
        throw new ArithmeticException("Division by zero");
        }
        
        StringBuilder quotient = new StringBuilder();
        String remainder = "";
        int decimalplaces = 0;
        boolean decimalPresent = false;

        for(int i=0; i< dividend.length();i++) {
            remainder = remainder + dividend.charAt(i);
            remainder = AInteger.removeLeadingZeros(remainder);

            int q =0;
            while(AInteger.compareStrings(remainder,divisor) >= 0) {
                remainder = AInteger.subtractStrings(remainder,divisor);
                q++;
            }
            quotient.append(q);
        }

        if(getDecimals && !remainder.equals("0")) {
            quotient.append(".");
            decimalPresent = true;

            while(!remainder.equals("0") && (decimalplaces < 30)){
                remainder = remainder + "0";
                remainder = AInteger.removeLeadingZeros(remainder);

                int q = 0;
                while (AInteger.compareStrings(remainder,divisor) >= 0) {
                    remainder = AInteger.subtractStrings(remainder,divisor);
                    q++;
                }
                quotient.append(q);
                decimalplaces++;
            }
        }
        return quotient.toString();
    }

    public AFloat divide(AFloat other) {
        if (other.totalValue.toString().equals("0")) 
        throw new ArithmeticException("Division by zero");

        boolean isNegative = this.totalValue.toString().startsWith("-") != other.totalValue.toString().startsWith("-");
        String dividend = this.totalValue.toString().replaceAll("^-", "");
        String divisor = other.totalValue.toString().replaceAll("^-", "");

        int scaleDiff = other.NumOfDigitsAfterDecimal - this.NumOfDigitsAfterDecimal;

        if(scaleDiff > 0) {
            for(int i = 0; i < scaleDiff ; i++) {
                dividend += "0";
            } 
        }
        else if(scaleDiff < 0) {
            for(int i = 0; i < -(scaleDiff); i++){
                divisor += "0";
            }
        }

        String quotientStr = divideStringsFloat(dividend, divisor, true);
        
        if (isNegative && !quotientStr.equals("0")) {
            quotientStr = "-" + quotientStr;
        }        

        if(quotientStr.contains(".")) {
            String[] parts = quotientStr.split("\\.");
            String intpart = parts[0];
            String fractpart = parts[1];

            AFloat result = new AFloat();
            result.totalValue = new AInteger(intpart + fractpart);
            result.NumOfDigitsAfterDecimal = fractpart.length();
            return result;
        }
        else {
            AFloat result = new AFloat();
            result.totalValue = new AInteger(quotientStr);
            result.NumOfDigitsAfterDecimal = 0;
            return result;
        }
    }

    public static void main(String[] args) {
        
        AFloat a = new AFloat("-1.000");
        AFloat b = new AFloat("15.00");
        AFloat result = a.divide(b);
        System.out.println(result); 
    
        
     }


    @Override
public String toString() {
    String numStr = this.totalValue.toString();
    boolean isNegative = numStr.startsWith("-");
    String absStr = isNegative ? numStr.substring(1) : numStr;
    
    // Handle case with no decimal places
    if (NumOfDigitsAfterDecimal == 0) {
        return numStr + ".0"; // Always include decimal point for AFloat
    }
    
    // Ensure we have enough digits for the decimal point
    while (absStr.length() <= NumOfDigitsAfterDecimal) {
        absStr = "0" + absStr;
    }
    
    // Insert decimal point
    int decimalPos = absStr.length() - NumOfDigitsAfterDecimal;
    StringBuilder result = new StringBuilder(absStr);
    result.insert(decimalPos, '.');
    
    // Add negative sign if needed
    if (isNegative) {
        result.insert(0, '-');
    }
    
    return result.toString();
}



}
