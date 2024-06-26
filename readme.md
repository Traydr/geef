# Geef

## Description

An image upload service. One which also displays other people images and lets them see yours.

## Requirements

### Task

On the back-end there is a graphical file storage with photos and a database that records which user
uploaded which file. The website should display a list of users. When a user is selected, thumbnails
of their uploaded files should be displayed.

### Recommended Technologies

REST web services

### System Capabilities

up to 300 users. Up to 50 files per user.

## Remaining Tasks

### Required
- [ ] Connect Minio and save files to it
- [ ] Update postgres db with:
  - [ ] Table for files
  - [ ] Table for showing which user owns the files and any metadata
  - [ ] Table for profiles
- [ ] Make sure auth is working w/ cookies and authenticate pages
- [ ] Frontend
  - [ ] Home
  - [ ] Signup / Login / Logout
    - [ ] Possibly in navbar / logout is only shown if user is logged in
  - [ ] Profile
    - [ ] Able to see all the images a person has uploaded
    - [ ] On your own profile, you are able to delete images
    - [ ] /profile gets your own profile
    - [ ] /profile/{public_user_id} gets someone else's profile
  - [ ] Discovery page (List of users)
  - [ ] Single image page
- [ ] APIs
  - [ ] Auth endpoints
  - [ ] Modify or rewrite image endpoints
  - [ ] Get all users / link profiles
  - [ ] Return components of all the images
  - [ ] For profiles return some profile data

### Optional:
- [ ] Optimizing images
- [ ] Following other users
