Feature: Create, Get and Delete AutoLink Reference and Delete a file


Scenario: CREATE A REPOSITORY FOR A AUTHENTICATED USER
Given Get Payload from "createRepo.json" for repo
When Create Repo "/user/repos" and provide token
Then Successfully Create with status code 201
And Varify owner Name


Scenario: CREATE AN AUTOLINK REFERENCE FOR A REPOSITORY
Given Should have repository and owner name
When Create an Autolink reference "/repos/" owner / repo "/autolinks" for a repository
Then Successfully Create an Autolink reference with status code 201
And Varify an Autolink id


Scenario: GET AN AUTOLINK REFERENCE FOR A REPOSITORY
Given should have an Autolink id
When Get an Autolink reference "/repos/" owner / repo "/autolinks/" id for a repository
Then Successfully get an Autolink reference with status code 200
And Verify response


Scenario: CREATE OR UPDATE FILE CONTENT
Given Get Payload from "newFileDetails.json" for create a file
When Create or update Execution "/repos/" owner / repo "/contents/" / "newfile.txt"
Then Successfully create a file with status code 201
And Verify sha


Scenario: DELETE A FILE
Given should have a file in Repository
When Delete a file "/repos/" owner / repo "/contents/" / "newfile.txt"
Then Successfully delete a file with status code 200


Scenario: DELETE FROM AN AUTOLINK REFERENCE FOR A REPOSITORY
Given should have Autolink id
When delete an Autolink reference "/repos/" owner / repo "/autolinks/" id for a repository
Then Successfully delete an Autolink reference with status code 204