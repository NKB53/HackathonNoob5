# HackathonNoob5
Team project for NMIT Hackathon 2019
AppName: Exam Helper

# About
* An app that is targeted towards students
* Compiles a set of question papers in text format
* Provides a list of recurring questions with their count 
* One tap online solution to any question
# Resources
1) Android Studio v3.2.1
2) Java Development Kit 12
3) Text files of Question papers
## Home Page
Here all we do is allow the user to select multiple question paper text files only

<img src ="https://user-images.githubusercontent.com/44922285/51634124-73740d00-1f79-11e9-87c5-1f6d6ac58b0d.jpeg" height="320" width="180">

## Settings page
We Allow the users to select the delimiters

<img src="https://user-images.githubusercontent.com/44922285/51634842-4163aa80-1f7b-11e9-9afb-807087d7cdba.jpeg" height="320" width="180">                               <img src="https://user-images.githubusercontent.com/44922285/51634712-f6e22e00-1f7a-11e9-8f4b-b75e248d1d7f.jpeg" height="320" width="180">

## Sample Input files
Three basic files to demonstrate basic app functionality

<img src="https://user-images.githubusercontent.com/44922285/51635561-11b5a200-1f7d-11e9-965b-e1205cfa6bfa.png" height="180" width="500">

## Output page
We display all questions in a list (where each list item is tapable for an online solution)in decreasing order of occurance.
Questions whoch are repeated at least once are only displayed.Questions that occur only once are not.The user can save the output
with any desired file name.

<img src="https://user-images.githubusercontent.com/44922285/51658436-0c347800-1fce-11e9-9777-f11beb01d7cd.jpeg" height="320" width="180">

## File Descriptions
## MainActivity.java
This activity contains a button to select multiple txt files from the device's internal storage and on selecting navigates to the QuestionsDisplay.java activity.

## QuestionsDisplay.java
This activity lists the most important questions defined as the ones that are recurring the maximum number of times. Questions which are not repeated will be ignored.
It also contains code to write the result into a text file located in the device's internal storage. The name of this file will be provided by the user.

## SettingsActivity.java
A basic empty activity that includes the SettingsFragment.

## SettingsFragment.java
An activity that gives the user to change delimiter settings(changing delimiter based on type of question paper) by means of sharedPreferences.

## QuestionsAdapter.java
A custom BaseAdapter which displays the sorted hashmap of questions with their respective count in a listView.

## AlgorithmUtils.java
A static java class which contains essential methods to check similarity of strings using levenshtein's distance algorithm, normalising strings and obtaining a sorted hashmap of questions and their respective count.
