public class MyInfArith {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java MyInfArith <type> <operation> <num1> <num2>");
            return;
        }

        String type = args[0].toLowerCase();
        String operation = args[1].toLowerCase();
        String num1 = args[2];
        String num2 = args[3];

        try {
            switch (type) {
                case "float":
                    arbitraryarithmetic.AFloat a = new arbitraryarithmetic.AFloat(num1);
                    arbitraryarithmetic.AFloat b = new arbitraryarithmetic.AFloat(num2);
                    arbitraryarithmetic.AFloat floatResult = null;

                    switch (operation) {
                        case "add":
                            floatResult = a.add(b);
                            break;
                        case "sub":
                            floatResult = a.subtract(b);
                            break;
                        case "mul":
                            floatResult = a.multiply(b);
                            break;
                        case "div":
                            floatResult = a.divide(b);
                            break;
                        default:
                            System.out.println("Invalid operation for float. Use add, sub, mul, divjava MyInfArith int mul.");
                            return;
                    }

                    System.out.println(floatResult);
                    break;

                case "int":
                case "integer":
                    arbitraryarithmetic.AInteger x = new arbitraryarithmetic.AInteger(num1);
                    arbitraryarithmetic.AInteger y = new arbitraryarithmetic.AInteger(num2);
                    arbitraryarithmetic.AInteger intResult = null;

                    switch (operation) {
                        case "add":
                            intResult = x.add(y);
                            break;
                        case "sub":
                            intResult = x.subtract(y);
                            break;
                        case "mul":
                            intResult = x.multiply(y);
                            break;
                        case "div":
                            intResult = x.divide(y);
                            break;
                        default:
                            System.out.println("Invalid operation for integer. Use add, subtract, multiply, divide.");
                            return;
                    }

                    System.out.println(intResult);
                    break;

                default:
                    System.out.println("Invalid type. Use 'float' or 'int'");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

