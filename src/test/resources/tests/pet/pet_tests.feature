Feature: Check pets

  @for_debug
  Scenario: Delete all pets
    Given get all pets with status = 'AVAILABLE'
    Then returns 200 and log message: '"Get pets by status AVAILABLE"'
    When delete pets

  Scenario Outline: Create and delete pet
    When create pet with params '<category>', '<name>', '<photos>', '<tags>', '<status>'
    Then returns 200 and log message: '"Create pet"'
    And verify pet by ID
    Then delete pet and verify it passed
    And verify that not found deleted pet
    Examples:
      |category|name|photos|tags|status|
      |dogs    |Buddy|https://side.com/photo1.jpg, https://side.com/photo1.jpg|1, "Nice"|SOLD|
      |cats    |1|https://side.com/photo1.jpg, https://side.com/photo1.jpg|1, "Nice"/2, "Black"|AVAILABLE|
      |rabbits |Mr. dj|https://side.com/photo1.jpg, https://side.com/photo1.jpg|1, "Nice"/2, "Black"|NULL|
      |lions   |NULL|https://side.com/photo1.jpg|1, "Nice"/2, "Black"|SOLD|
      |lions   |Buddy|https://side.com/photo1.jpg|NULL|AVAILABLE|
      |lions   |Buddy|NULL|1, "Nice"/2, "Black"|PENDING|
      |NULL    |1|NULL|1, "Nice"|NULL|
      |lions   |e|https://side.com/photo1.jpg, https://side.com/photo1.jpg|NULL|NULL|

  Scenario: Get pet by status
    When create pet with params category='dogs', name='Buddy', photos='https://example.com/photo1.jpg', tags='1, "Nice"/2, "Black"', status='AVAILABLE'
    Then returns 200 and log message: '"Create pet"'
    When get pet by status='AVAILABLE'
    Then returns 200 and log message: '"Get pets by status AVAILABLE"'
    And verify count responses

  Scenario: Update pet
    When create pet with params category='dogs', name='Buddy', photos='https://example.com/photo1.jpg', tags='1, "Nice"/2, "Black"', status='AVAILABLE'
    Then returns 200 and log message: '"Create pet"'
    And verify pet by ID
    When update pet with new params category='lions', name='new name', photos='https://example.com/photo2.jpg', tags='3, "Small"', status='PENDING'
    Then returns 200 and log message: '"Update pet"'
    And verify pet by ID from the first created pet

  Scenario: Update pet in store
    When create pet with params category='dogs', name='Buddy', photos='https://example.com/photo1.jpg', tags='1, "Nice"/2, "Black"', status='AVAILABLE'
    Then returns 200 and log message: '"Create pet"'
    And verify pet by ID
    When update pet in store with params name='new name', status='SOLD'
    Then returns 200 and log message: '"Update pet"'
    And verify pet by ID from the first created pet

  Scenario: Update pet image
    When create pet with params category='cats', name='Mur', photos='https://example.com/photo1.jpg', tags='1, "Nice"/2, "Black"', status='AVAILABLE'
    Then returns 200 and log message: '"Create pet"'
    When upload image '"src/test/resources/cat.jpg"'
    Then returns 200 and log message: '"Upload pet image"'