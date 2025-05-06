# Game Night Client

## Table of Contents
- [Overview](#overview)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Installation Guide](#installation-guide)
- [Features](#features)
- [Intended Users](#intended-users)
- [Contributors](#contributors)

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

## Intended Users
- Board game players: People who play board games with friends and family
- Board game collectors: People who own multiple board games and bring them places

## Contributors
- Kathleen Wims [GitHub](https://github.com/bewimsical)  
- Darianne Ramos [GitHub](https://github.com/darianne123)  
- Kenneth LeGare [GitHub](https://github.com/DataHiveMind)  
- Joshua Samson [GitHub](https://github.com/jsams909)  
- Efrat Weiss [GitHub](https://github.com/Wieefi)
