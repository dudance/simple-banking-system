# simple-banking-system
> Simple banking system with the use of SQLite. It provides simple bank operations like create new account, add income, transfer money or check account balance.

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Usage](#usage)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Contact](#contact)
<!-- * [License](#license) -->


## General Information
The aim of the project was to learn the ability to use simple SQL commands in combination with java.


## Technologies Used
- Java
- SQLite
- Singleton design pattern
- Luhn Algorithm


## Features
List the ready features here:
- Creating account and login using card number and pin
- Generating the card number using the luhn algorithm
- Checking account balance
- Possibility to close (remove) account
- Possibility to add income to account
- Possibility to transfer money using receiver's card number

## Screenshots
![Example screenshot](./img/screenshot.png)
<!-- If you have screenshots you'd like to share, include them here. -->


## Setup
Clone this repo to your desktop. Next, you should read the database file name from the command line argument. Filename should be passed to the program using -fileName argument, for example, -fileName db.s3db.


## Usage
After read the database file, just run Main.java. Then, in accordance with the menu displayed, press the appropriate button on the keyboard assigned to the appropriate operation.

## Project Status
Project is: _complete_

## Room for Improvement

Room for improvement:
- adding different currencies
