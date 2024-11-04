if exist "src\main\resources\private-key.pem" del "src\main\resources\private-key.pem"
if exist "src\main\resources\public-key.pem" del "src\main\resources\public-key.pem"
openssl genpkey -algorithm RSA -out "src\main\resources\private-key.pem"
openssl rsa -pubout -in "src\main\resources\private-key.pem" -out "src\main\resources\public-key.pem"
@REM openssl pkcs8 -topk8 -inform PEM -outform PEM -in private-key.pem -out private-key.pem -nocrypt
