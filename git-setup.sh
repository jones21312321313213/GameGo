#!/bin/bash

# Prompt for user name and email
read -p "Enter your Git user name: " username
read -p "Enter your Git email: " useremail
# Set global Git identity
git config --global user.name "$username"
git config --global user.email "$useremail"

echo "✅ Git global identity set:"
git config --global user.name
git config --global user.email

echo "Now you can commit using:"
echo 'git commit -m "your commit message"'
