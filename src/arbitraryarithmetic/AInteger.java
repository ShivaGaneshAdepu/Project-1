package arbitraryarithmetic;

public class AInteger {
    private String value;
    private boolean isPositive;
    
    public static String removeLeadingZeros(String numStr) {
        if (numStr == null || numStr.isEmpty()) return "0";
        int i = 0;
        while (i < numStr.length() && numStr.charAt(i) == '0') {
            i++;
        }
        String cleaned = numStr.substring(i);
        return cleaned.isEmpty() ? "0" : cleaned;
    }

    public AInteger() {
        this.value = "0";
        this.isPositive = true;
    }

    public AInteger(String s) {
        if(s == null || s.isEmpty())
        throw new IllegalArgumentException("Empty string");

        int start = 0;
        if (s.charAt(0) == '-') {
            this.isPositive = false;
            start = 1;
        } else {
            this.isPositive = true;
        }

        String num = s.substring(start);
        this.value = removeLeadingZeros(num);
        if (this.value.equals("0")) this.isPositive = true;
    }

    public AInteger(AInteger other) {
        this.value = other.value;
        this.isPositive = other.isPositive;
    }

    public static AInteger parse(String s) {
        return new AInteger(s);
    }

    public static int compareStrings(String a, String b) {
        a = removeLeadingZeros(a);
        b = removeLeadingZeros(b);
        if (a.length() != b.length()) {
            return a.length() > b.length() ? 1 : -1;
        }
        return a.compareTo(b);
    }

    private static String addStrings(String num1, String num2) {
        StringBuilder shiv = new StringBuilder();
        int carry = 0;
        int i = num1.length()-1;
        int j = num2.length()-1;
        int maxLength = Math.max(num1.length(), num2.length());

        for (int k = 0; k < maxLength; k++) {
            int digitA = (i >= 0) ? (num1.charAt(i--) - '0') : 0;
            int digitB = (j >= 0) ? (num2.charAt(j--) - '0') : 0;
            int sum = digitA + digitB + carry;
            shiv.append(sum % 10);
            carry = sum / 10;
        }

        if (carry > 0) {
            shiv.append(carry);
        }

        return shiv.reverse().toString();
    }

    public static String subtractStrings(String num1, String num2) {
        StringBuilder shiv = new StringBuilder();
        int borrow = 0;
        int i = num1.length()-1;
        int j = num2.length()-1;
        while (i >= 0) {
            int digitA = num1.charAt(i) - '0' - borrow;
            int digitB = (j >= 0) ? (num2.charAt(j--) - '0') : 0;
            if (digitA < digitB) {
                digitA += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            shiv.append(digitA - digitB);
            i--;
        }
        return removeLeadingZeros(shiv.reverse().toString());
    }

    public AInteger add(AInteger other) {
        if (this.isPositive == other.isPositive) {
            String sum = addStrings(this.value, other.value);
            AInteger res = new AInteger(sum);
            res.isPositive = this.isPositive;
            return res;
        } else {
            // a + (-b) = a - b
            int cmp = compareStrings(this.value, other.value);
            if (cmp == 0) return new AInteger("0");
            if (cmp > 0) {
                String diff = subtractStrings(this.value, other.value);
                AInteger res = new AInteger(diff);
                res.isPositive = this.isPositive;
                return res;
            } else {
                String diff = subtractStrings(other.value, this.value);
                AInteger res = new AInteger(diff);
                res.isPositive = other.isPositive;
                return res;
            }
        }
    }

    public AInteger subtract(AInteger other) {
        if (this.isPositive != other.isPositive) {
            // a - (-b) = a + b and (-a) - (+b) = -a - b
            String sum = addStrings(this.value, other.value);
            AInteger res = new AInteger(sum);
            res.isPositive = this.isPositive;
            return res;
        } else {
            // a - b
            int cmp = compareStrings(this.value, other.value);
            if (cmp == 0) return new AInteger("0");
            if (cmp > 0) {
                String diff = subtractStrings(this.value, other.value);
                AInteger res = new AInteger(diff);
                res.isPositive = this.isPositive;
                return res;
            } else {
                String diff = subtractStrings(other.value, this.value);
                AInteger res = new AInteger(diff);
                res.isPositive = !this.isPositive;
                return res;
            }
        }
    }

    private static String multiplyStrings(String num1, String num2) {
        int i = num1.length();
        int j = num2.length();
        int []res = new int [i+j];

        for(int m = i-1; m >=0; m--) {
            int digitA = num1.charAt(m) - '0';
            for(int n = j-1; n>=0; n--) {
                int digitB = num2.charAt(n) - '0';
                int sum = res[m+n+1] + digitA * digitB ;
                res[m+n+1] = sum%10;
                res[m+n] += sum/10;
            }
        }

        StringBuilder shiv = new StringBuilder();
        for(int num : res) shiv.append(num);
        return removeLeadingZeros(shiv.toString());
    }

    public AInteger multiply(AInteger other) {
        String prod = multiplyStrings(this.value, other.value);
        AInteger res = new AInteger(prod);
        if(prod.equals("0")){
        res.isPositive = true;
        }
        else if(this.isPositive == other.isPositive){
            res.isPositive = true;
        }
        else {
            res.isPositive = false;
        }
        return res;
    }

    private static String divideStrings(String dividend, String divisor) {
            if(divisor.equals("0")) throw new ArithmeticException("Division by Zero");
            if(compareStrings(dividend, divisor) < 0) return "0";
            StringBuilder quotient = new StringBuilder();
            String remainder = "";
            for(int i = 0; i < dividend.length(); i++) {
                remainder = remainder + dividend.charAt(i);
                remainder = removeLeadingZeros(remainder);
                int q = 0;
                while(compareStrings(remainder, divisor) >= 0) {
                    remainder = subtractStrings(remainder, divisor);
                    q++;
                }
                quotient.append(q);
            }
            return removeLeadingZeros(quotient.toString());
    }

    public AInteger divide(AInteger other) {
        if(other.value.equals("0")) throw new ArithmeticException("Division by Zero");
        String quot = divideStrings(this.value, other.value);
        AInteger res = new AInteger(quot);
        if(this.isPositive == other.isPositive) {
            res.isPositive = true;
        }
        else{
            res.isPositive = false;
        }
        return res;
    }


public static void main(String[] args) {
    
    AInteger a = new AInteger("426");
    AInteger b = new AInteger("-3");
    AInteger result = a.divide(b);
    System.out.println(result); 

    
 }

 
 @Override
public String toString() {
    return (isPositive ? "" : "-") + value;
}

}