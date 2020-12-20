#!/bin/bash

# Check if pwd is located inside the SkillsList folder
if [ $(basename "$PWD") != "SkillsList" ]; then
    echo "Invalid current directory! Please cd to the SkillsList directory and re-run this script."
    exit 1
fi

# Check if the deploy script is already running
for pid in $(pidof -x skillslist_server.sh); do
    echo $pid
    if [ $pid != $$ ]; then
        echo "SkillsList server already running!"
        exit 2
    fi
done

# Check if the server is already running and ask for killing if yes
PID_FILE="server/outputs/pid.txt"
if test -f "$PID_FILE"; then
    echo $PID_FILE
    if kill -0 $(head -n 1 $PID_FILE) > /dev/null 2>&1; then
        read -p "SkillsList server is already running. Do you want to kill it and continue? (y/n)" -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]
        then
            kill $(head -n 1 $PID_FILE)
        else
            exit 3
        fi
    fi
fi

# Reset and update the project
git reset --hard
git pull

# Install and update Maven dependencies if necessesary
cd server
mvn install

# Execute SkillsList server in background task and redirect its output
mkdir -p outputs
echo "SkillList server launched!"
mvn exec:java -Dexec.mainClass="com.bitsplease.skillslist_server.SkillslistServerApplication" &> outputs/skilllist_server.txt & echo $! > outputs/pid.txt
