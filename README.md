# AndroidPractiScoreUploader

An Android application for uploading [PractiScore]( https://practiscore.com/) result data for IPSC shooting matches to a server ([IPSCResultServer](https://github.com/jarnovirta/IPSCResultServer), [hitfactor.fi](hitfactor.fi)).
The application reads match result data from a PractiScore match export file (*.psc) and sends it to the server for publication.

The project is an Android Studio / Gradle project.

## Setting up a connection to a server

To set up a connection to the ([IPSCResultServer](https://github.com/jarnovirta/IPSCResultServer)). In the application’s main vies, select “Edit connection”. 
Enter the server’s address, a username and the password. Select “Ok”. 

To test the connection select “Test Connection” in the application’s main view. 

## Selecting PractiScore match data to upload

In PractiScore, export the match to be uploaded by selecting “Export” in PractiScore’s main view, then “Save to SD Card”.
In the PractiScore Uploader application’s main view, select “Select PractiScore Export File” and select the relevant export file (.psc). It should be under Internal Storage/PractiScore/Match.

## Uploading match data

You can upload the selected match data by selecting “Send data”. 

Alternatively, you can set the PractiScore Uploader application to track changes in the selected PractiScore export file by selecting “Tracking [on/off]”. The application will automatically upload the match data when you make a new match data export in PractiScore. 
