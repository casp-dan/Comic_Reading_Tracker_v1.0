The first version of my comic reading tracking app. This version is entirely in Java, interfacing with a SQL Database. The structure for the database is included in src/main/SQL. 

This app can:
- track all singular comic issues being read
- present all issues of any series in the database as well as the date each issue has been read
- present every issue read on a given date indicating the series name and issue number
- provide stats on reading habits by publisher. Can filter for individual months, years, or between any two given dates.

This was meant as a replacement for my previous method of tracking and as such is designed to only present information as early as 1/1/2022. This can be changed by modifying the START constant in "src\main\java\models\Date.java"

Launches as a gradle app.
