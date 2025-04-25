#!/bin/bash

# Set PIT JAR path
PIT_JAR="pitest-1.15.3-jar-with-dependencies.jar"

if [ ! -f "$PIT_JAR" ]; then
    echo "PIT JAR file not found: $PIT_JAR"
    exit 1
fi

# We need to ensure the JUnit JAR is also in our classpath
JUNIT_JAR="target/test-classes"

# Run PIT with absolute path to jar file
java -cp "./target/classes:./randoop-compiled:$JUNIT_JAR:./$PIT_JAR" org.pitest.mutationtest.commandline.MutationCoverageReport \
    --reportDir=target/pit-reports-randoop \
    --targetClasses=com.company.Booking,com.company.BookingDatabaseHelper,com.company.BookingIterator,com.company.FacultyMember,com.company.Manager,com.company.NonFacultyStaff,com.company.ParkingLot,com.company.ParkingLotManager,com.company.ParkingSpace,com.company.ParkingSpaceIterator,com.company.Payment,com.company.PriceCalculator,com.company.RegisterationSystem,com.company.SensorData,com.company.Student,com.company.StrongPasswordRecognizer,com.company.TimerTask,com.company.User,com.company.UserDatabaseHelper,com.company.UserFactory,com.company.Visitor \
    --targetTests=RegressionTest,RegressionTest0,RegressionTest1,RegressionTest2,RegressionTest3,RegressionTest4,RegressionTest5,RegressionTest6,RegressionTest7,RegressionTest8,RegressionTest9,RegressionTest10,RegressionTest11,RegressionTest12,RegressionTest13,RegressionTest14,RegressionTest15,RegressionTest16,RegressionTest17,RegressionTest18,RegressionTest19,RegressionTest20,RegressionTest21,RegressionTest22,ErrorTest,ErrorTest0 \
    --sourceDirs=src \
    --classPath=./target/classes,./randoop-compiled,./target/test-classes \
    --verbose \
    --outputFormats=HTML,XML \
    --timestampedReports=false 