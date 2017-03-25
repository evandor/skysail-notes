@notesApplication
Feature: [Application Notes] - notes specific features - HTML 

    An note...
    
    These tests assume that the user is authenticated and has the appropriate roles; initially,
    the database is empty (but there is no guarantee of the execution order of the tests).
    
    The media type used here is not relevant as nothing gets actually rendered in these tests.

Background: 
    Given a running NotesApplication
    And I am logged in as 'admin'

@Creation
Scenario: adding a simple note entity
    When I add a note like this:
      | content | content_<random> |
    And I query all notes
    Then the notes list page contains such a note:
       | content      | content_<random> |

@Creation    
Scenario: getting "Created 201" after creating a new note
    When I add a note like this:
      | content | content_<random> |
    Then I get a 'Created (201)' response

@Validation
Scenario: adding a simple account without name yields an error
    When I add a note like this:
      | content | |
    Then I get a response containing 'Bad Request'

@Retrieval
Scenario: retrieving a created note again
    When I add a note like this:
      | content | content_abc |
    And I open the note page
    Then the page contains 'content_abc'

@Updating
Scenario: updating an account entity
    When I add a note like this:
    | content | content_<random> |
    And I change its 'content' to 'content_xxx'
    And I open the note page
    Then the page contains 'content_xxx'
    
@Security
Scenario: if the user sends the "created" property it should be ignored
    When I add a note like this:
       | content | content_<random> |
       | created | 2000-10-01 10:11:12    |
    And I open the note page
    Then the page contains:
       | content   | content_<random> |
       | created   | <!>2000-10-01 10:11:12 |
    