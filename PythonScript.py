import os,sys,subprocess

if len(sys.argv) != 5:
    print("Invalid input format")
    exit
    
numtype = sys.argv[1]
operation = sys.argv[2]
num1 = sys.argv[3]
num2 = sys.argv[4]    

# os.system("ant jar")

shiva = ["java", "-jar", "src/arbitraryarithmetic/arithmetic.jar", numtype, operation, num1, num2]

subprocess.run(shiva)
   