@echo off
REM Script to verify and fix common code issues

echo ===========================================
echo Code verification and correction tool
echo ===========================================

echo.
echo 1. Checking code style (Checkstyle)
call mvn checkstyle:check

echo.
echo 2. Formatting code and organizing imports
call format-code.cmd

echo.
echo 3. Checking security issues (SpotBugs)
call mvn spotbugs:check

echo.
echo 4. Checking vulnerabilities (OWASP Dependency Check)
call mvn dependency-check:check

echo.
echo 5. Running tests
call mvn test

echo.
echo ===========================================
echo Verification completed!
echo ===========================================