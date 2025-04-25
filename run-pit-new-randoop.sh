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
    --reportDir=target/pit-reports-new-randoop \
    --targetClasses=com.company.Booking,com.company.BookingDatabaseHelper,com.company.BookingIterator,com.company.FacultyMember,com.company.Manager,com.company.NonFacultyStaff,com.company.ParkingLot,com.company.ParkingLotManager,com.company.ParkingSpace,com.company.ParkingSpaceIterator,com.company.Payment,com.company.PriceCalculator,com.company.RegisterationSystem,com.company.SensorData,com.company.Student,com.company.StrongPasswordRecognizer,com.company.TimerTask,com.company.User,com.company.UserDatabaseHelper,com.company.UserFactory,com.company.Visitor \
    --targetTests=RegressionTest0 \
    --sourceDirs=src \
    --classPath=./target/classes,./target/test-classes \
    --verbose \
    --outputFormats=HTML,XML \
    --timestampedReports=false 