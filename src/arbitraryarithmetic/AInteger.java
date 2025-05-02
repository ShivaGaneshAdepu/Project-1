package arbitraryarithmetic;

public class AInteger {
    private String value; //We are Storing the number as a string to handle arbitrary precision 
    private boolean isPositive; //to keep track of the sign of the given string
    

    // Helper method: Removing leading zeros from a string to clean up the number representation
    // This is used after operations or when parsing input to ensure no unnecessary zeros
    public static String removeLeadingZeros(String numStr) {
        if (numStr == null || numStr.isEmpty()) return "0";
        int i = 0;
        // Loop through the string to skip all leading zeros
        while (i < numStr.length() && numStr.charAt(i) == '0') {
            i++;
        }
        // Take the substring after leading zeros, return "0" if the result is empty
        String cleaned = numStr.substring(i);
        return cleaned.isEmpty() ? "0" : cleaned;
    }
    // Default constructor: Setting the value to "0" and sign to positive
    public AInteger() {
        this.value = "0";
        this.isPositive = true;
    }

    // Constructor with string input: Parsing a string to create an AInteger object
    public AInteger(String s) {
        // Checking for invalid input (null or empty string)
        if(s == null || s.isEmpty())
        throw new IllegalArgumentException("Empty string");

        int start = 0;
        // Checking if the number is negative by looking for a leading minus sign
        if (s.charAt(0) == '-') {
            this.isPositive = false;
            start = 1;
        } else {
            this.isPositive = true;
        }
        // Extract the numeric part and remove leading zeros
        String num = s.substring(start);
        this.value = removeLeadingZeros(num);
        // If the value is "0", ensure the sign is positive (no negative zero)
        if (this.value.equals("0")) this.isPositive = true;
    }


    // Copy constructor: Creating a new AInteger by copying another AInteger's value and sign
    public AInteger(AInteger other) {
        this.value = other.value;
        this.isPositive = other.isPositive;
    }

    // Static method: Parsing a string to create and return a new AInteger object
    public static AInteger parse(String s) {
        return new AInteger(s);
    }


    // Helper method: Comparing two strings representing numbers
    // Returns 1 if a > b, -1 if a < b, or 0 if a == b based on length and digit comparison
    public static int compareStrings(String a, String b) {
        // Remove leading zeros from both strings for accurate comparison
        a = removeLeadingZeros(a);
        b = removeLeadingZeros(b);
        // Compare lengths first—if they're different, the longer number is larger
        if (a.length() != b.length()) {
            return a.length() > b.length() ? 1 : -1;
        }
        // If lengths are equal, compare the strings lexicographically
        return a.compareTo(b);
    }

    // Helper method: Adding two strings representing numbers digit by digit
    private static String addStrings(String num1, String num2) {
        // Using StringBuilder to build the result
        StringBuilder shiv = new StringBuilder();
        int carry = 0;
        int i = num1.length()-1;
        int j = num2.length()-1;
        int maxLength = Math.max(num1.length(), num2.length());

        for (int k = 0; k < maxLength; k++) {
            // Get digits from both numbers, use 0 if we've run out of digits
            int digitA = (i >= 0) ? (num1.charAt(i--) - '0') : 0;
            int digitB = (j >= 0) ? (num2.charAt(j--) - '0') : 0;
            int sum = digitA + digitB + carry;
            // Append the last digit of the sum and update the carry
            shiv.append(sum % 10);
            carry = sum / 10;
        }

        if (carry > 0) {
            shiv.append(carry);
        }

        // Reverse the result since we built it from right to left
        return shiv.reverse().toString();
    }


    // Helper method: Subtracting two strings representing numbers (assuming num1 >= num2)
    public static String subtractStrings(String num1, String num2) {
        // Using StringBuilder to build the result
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
            // Append the difference for this digit
            shiv.append(digitA - digitB);
            i--;
        }
        // Reverse and remove leading zeros from the result
        return removeLeadingZeros(shiv.reverse().toString());
    }

    // Method: Adding this AInteger with another AInteger
    public AInteger add(AInteger other) {
        // If signs are the same, add the numbers directly
        if (this.isPositive == other.isPositive) {
            String sum = addStrings(this.value, other.value);
            AInteger res = new AInteger(sum);
            res.isPositive = this.isPositive;
            return res;
        } else {
            // If signs differ, convert to subtraction
            // a + (-b) = a - b
            int cmp = compareStrings(this.value, other.value);
            if (cmp == 0) return new AInteger("0");
            // If this number is larger, subtract other from this
            if (cmp > 0) {
                String diff = subtractStrings(this.value, other.value);
                AInteger res = new AInteger(diff);
                res.isPositive = this.isPositive;
                return res;
            } else {
                // If other number is larger, subtract this from other
                String diff = subtractStrings(other.value, this.value);
                AInteger res = new AInteger(diff);
                res.isPositive = other.isPositive;
                return res;
            }
        }
    }

    // Method: Subtracting another AInteger from this AInteger
    public AInteger subtract(AInteger other) {
        // If signs are different, convert to addition
        if (this.isPositive != other.isPositive) {
            // a - (-b) = a + b and (-a) - (+b) = -a - b
            String sum = addStrings(this.value, other.value);
            AInteger res = new AInteger(sum);
            res.isPositive = this.isPositive;
            return res;
        } else {
            //If signs are the same, subtract the smaller from the larger
            // a - b
            int cmp = compareStrings(this.value, other.value);
            if (cmp == 0) return new AInteger("0");
            // If this number is larger, subtract other from this
            if (cmp > 0) {
                String diff = subtractStrings(this.value, other.value);
                AInteger res = new AInteger(diff);
                res.isPositive = this.isPositive;
                return res;
            } else {
                // If other number is larger, subtract this from other and flip the sign
                String diff = subtractStrings(other.value, this.value);
                AInteger res = new AInteger(diff);
                res.isPositive = !this.isPositive;
                return res;
            }
        }
    }

    // Helper method: Multiplying two strings representing numbers using schoolbook multiplication
    private static String multiplyStrings(String num1, String num2) {
        int i = num1.length();
        int j = num2.length();
        // Array to store the result digits (maximum length is i+j)
        int []res = new int [i+j];

        // Multiply each digit of num1 with each digit of num2
        for(int m = i-1; m >=0; m--) {
            int digitA = num1.charAt(m) - '0';
            for(int n = j-1; n>=0; n--) {
                int digitB = num2.charAt(n) - '0';
                int sum = res[m+n+1] + digitA * digitB ;
                res[m+n+1] = sum%10;
                res[m+n] += sum/10;
            }
        }

        // Build the result string from the array
        StringBuilder shiv = new StringBuilder();
        for(int num : res) shiv.append(num);
        return removeLeadingZeros(shiv.toString());
    }

    // Method: Multiplying this AInteger with another AInteger
    public AInteger multiply(AInteger other) {
        // Multiply the string values
        String prod = multiplyStrings(this.value, other.value);
        AInteger res = new AInteger(prod);
        // Set the sign: positive if signs are the same, negative if different
        // Special case: if the product is 0, sign is positive
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
    
    // Helper method: Dividing two strings representing numbers using long division
    private static String divideStrings(String dividend, String divisor) {
        // Check for division by zero
            if(divisor.equals("0")) throw new ArithmeticException("Division by Zero");
            // If dividend is smaller than divisor, result is 0
            if(compareStrings(dividend, divisor) < 0) return "0";
            // Build the quotient digit by digit
            StringBuilder quotient = new StringBuilder();
            String remainder = "";
            // Process each digit of the dividend
            for(int i = 0; i < dividend.length(); i++) {
                remainder = remainder + dividend.charAt(i);
                remainder = removeLeadingZeros(remainder);
                int q = 0;
                // Subtract divisor from remainder as many times as possible
                while(compareStrings(remainder, divisor) >= 0) {
                    remainder = subtractStrings(remainder, divisor);
                    q++;
                }
                quotient.append(q);
            }
            // Remove leading zeros from the quotient
            return removeLeadingZeros(quotient.toString());
    }

    public AInteger divide(AInteger other) {
        // Check for division by zero
        if(other.value.equals("0")) throw new ArithmeticException("Division by Zero");
        // Divide the string values
        String quot = divideStrings(this.value, other.value);
        AInteger res = new AInteger(quot);
        // Set the sign: positive if signs are the same, negative if different
        if(this.isPositive == other.isPositive) {
            res.isPositive = true;
        }
        else{
            res.isPositive = false;
        }
        return res;
    }

//Just Testing the AInteger class with a division example
public static void main(String[] args) {
    
    AInteger a = new AInteger("426");
    AInteger b = new AInteger("-3");
    AInteger result = a.divide(b);
    System.out.println(result); 

    
 }

 // Overriding toString: Converting the AInteger to a string with the correct sign
 @Override
public String toString() {
    return (isPositive ? "" : "-") + value;
}

}