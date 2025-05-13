# Game Night Client

## Table of Contents
1. [Overview](#overview)
2. [Technologies Used](#technologies-used)
3. [Setup](#setup)
4. [Installation Guide](#installation-guide)
5. [Features](#features)
6. [Intended Users](#intended-users)
7. [How It Works](#how-it-works)
   - [Splash Page](#splash-page)  
   - [Login Page](#login-page)  
   - [Create Account Page](#create-account-page)  
   - [Parties Page](#parties-page)
   - [Create Party Screen](#create-party-screen)
   - [Party Specific Page](#party-specific-page)
   - [Friends Page](#friends-page)
   - [Games Page](#games-page) 
   - [Profile Page](#profile-page) 
9. [Contributors](#contributors)

## Overview
Gamenight is a SAAS (software as a service) application that allows people to log what board games they own, add friends to their account, and then link board game libraries with said users friends in order to plan a game night.  
The planning phase involves everyone seeing each other's libraries and voting on what games should be played. There are also fields to name the group, set a time for the meeting, and pick where the meeting is taking place.  
The technologies Game Night operates with are Java/JavaFX for the front end and back end, Microsoft Azure for the database and authentication services, and Java Spring Boot to implement any API needs.

## Technologies Used
- Java 
- JavaFX  
- Figma
- Spring Boot
- Board Game Geek API
- Microsoft Azure

## Setup
Prerequisites:
- Java Development Kit  
  - Version: 23 for front end  
    - [Download JDK 23 Here](https://jdk.java.net/java-se-ri/23)  
  - Version: 21 for back end  
    - [Download JDK 21 Here](https://jdk.java.net/java-se-ri/21)
- Apache Maven  
  - [Download Apache Maven Here](https://maven.apache.org/install.html)

## Installation Guide
1. Clone this Repository
2. Clone the server Repository  
   - [GameNightServer Repo](https://github.com/bewimsical/GameNightServer)
3. Build the Projects
4. Run the Projects  
   - Server First  
   - Then Client

## Features
- Login/Register: Create an account uploaded to our database and sign into your account to save application relevant information.
- Games Page: Search and view board games pulled from the Board Games Geek API and add them to your account.
- Friends Page: Search and add other users to your friends list so you can add them to your party later.
- General Party Page: View and Create parties, pick a time, pick a place, and add it to your page. 
- Specific Party Page: Click on one of your parties to pull up a new page where you can add friends and look at what games you each own before you vote on what to play.
- Profile Page: View your parties, friends, and games all in one place

## Intended Users
- Board game players: People who play board games with friends and family
- Board game collectors: People who own multiple board games and bring them places

## How it Works


## Splash Page 
![splash page gif game nigth](https://github.com/user-attachments/assets/74a497b8-f82a-4d56-9345-555440321423)

Game Night's animated Splash Page that can take you to either our login or create account pages.

## Login Page
![login page game night](https://github.com/user-attachments/assets/60f3278a-cd3a-48b7-b287-0b1632652e11)

Game Night's login page that uses an email and password (that is hashed) stored in the database to log the user in and bring them to their parties page. If the password is incorrect the user will recieve an alert. There is also a text button that allows a user to go to the create account page directly from the login page.

## Create Account Page
![create account page game night](https://github.com/user-attachments/assets/6cf445d5-dc47-47ed-bbeb-b66af68ca47e)

Game Night's account requires a first name, last name, username, email and a password that must be reconfirmed in order to create an account. If all fields are correctly made then after hitting the create account button an alert will pop up saying the account was successfully created, assining the user a random profile picture, and the user will be brought back to the sign in page. If the user information belongs to another account and or the password fields do not match an alert will pop up saying the account cannot be created. There is also a text button that can bring the user back to login anytime they want.

## Parties Page
![parties page game night](https://github.com/user-attachments/assets/6fd2d496-e5f1-4706-8f73-66bb96956384)

The parties page is a hub showing all the users upcoming parties displaying the date, time, name, and filled in location of the party. The right hand side has a interactive calendar that highlights the current date as well as any dates with upcoming parties. Below the calendar is two drop down menus, one for the current selected date on the calendar and if there is on parties on said date, and a drop down for any past parties the user is in.

## Create Party Screen
![create new party page](https://github.com/user-attachments/assets/3edcc6b3-a164-4ff8-aa7d-32c513b3ca3d)

Creating a party allows the user to name their party, use a map to pick a date, use a drop down to pick a specific time, and then has a text field to name the location of their choosing.


## Party Specific Page
![party specifc gif](https://github.com/user-attachments/assets/1be3310c-9705-4da5-bc5a-5d149e241644)

## Friends Page
![friends page game night](https://github.com/user-attachments/assets/92b6c80a-0a6d-4530-8708-98315292d54f)

The friends page displays all the users current friends and has a search bar that allows the user to search the username of any other user on the Game Night database.

Descript

## Games Page
![games page gif game nigth](https://github.com/user-attachments/assets/44bedc80-3ecb-4823-94d2-7d825f499fab)

The games page displays all the users current games and has a search bar for the user to search games all pulled from the Board Game Geek API.

## Profile Page
![profile page game night](https://github.com/user-attachments/assets/7337b19c-569e-4727-84ef-b1a054dbf937)

Descript









## Contributors
- Kathleen Wims [GitHub](https://github.com/bewimsical)  
- Darianne Ramos [GitHub](https://github.com/darianne123)  
- Kenneth LeGare [GitHub](https://github.com/DataHiveMind)  
- Joshua Samson [GitHub](https://github.com/jsams909)  
- Efrat Weiss [GitHub](https://github.com/Wieefi)
