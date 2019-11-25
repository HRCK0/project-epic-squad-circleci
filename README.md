# eCare Inc.

## Team Members

[![Build Status](https://circleci.com/gh/professor-forward/project-epic-squad.png?branch=master)](https://circleci.com/gh/professor-forward/project-epic-squad)

| Name | Student Number |
| --- | --- |
| Ruwani De Alwis | 300057076 |
| Mustafa Ali  | 300060406 |
| Georges Chamoun | 300060437 |
| Gabriel Granata | 300057462
| Vanja Prokic | 300057417 |

# Deliverable 1

For screenshots related to Deliverable 1, please see [the screenshot folder]( https://github.com/professor-forward/project-epic-squad/blob/f/deliverable01/screenshots/output.md) 

 [APK File]( https://github.com/professor-forward/project-epic-squad/blob/f/deliverable01/app-debug.apk). 
 
# Deliverable 2

### Admin Login:
- Username: admin@ecare.com
- Password: 5T5ptQ

#### Employee Login:
- Username: rdeal081@uottawa.ca
- Password: seg2105


### Relevant Information 
By logging in with admin, you will be able to add, edit and delete serivces. To see how the services screen behaves with employees/patients, please login using the employee credentials.

- The [UML Diagram](https://github.com/professor-forward/project-epic-squad/tree/f/deliverable02/docs/UML)
- The [APK](https://github.com/professor-forward/project-epic-squad/blob/f/deliverable02/app-debug.apk)


#### Testing with Espresso
Many of our tests involved passing intents to other activites, clicking buttons, Espresso was the more useful tool as it helped us properly test our methods and allowed more functionality than JUNIT, therefore most tests are completed using espresso. To check the junit tests for deliverable 1 content, please look at LoginActivityTest. 

To test espresso tests for deliverable 1 contents (login) please run LoginActivityTest2. To test espresso tests for deliverable 2 tasks, please run ServicesActivityTest. There is a method runAllServiceFunctions() witin the test file, which calls the other 3 tests. To avoid clogging the backend and to see the full process of adding,editing and deleting the same service, please run that method only as it does call all the other relevant tests. 

# Deliverable 3

#### Employee Login:
- Username: rdeal081@uottawa.ca
- Password: seg2105

- Username: gabriel@ecare.com
- Password: password

### Relevant Information 
By loggining in as an employee, you will be directed to the welcome screen with the additional options: complete user profile, which includes additional information about the employee (name of company, address, licensed etc), add services (from the list of services provided by the admin), add availability. The employee will not be able to add services or add their availabilty until they complete the profile. The user is able to view and edit their services and availability. 
 
 - The [UML Diagram](https://github.com/professor-forward/project-epic-squad/tree/f/deliverable03/docs/UML)
 - The [APK](https://github.com/professor-forward/project-epic-squad/blob/f/deliverable03/app-debug.apk)


#### Testing 

Two tests were run, one to see if an employee's profile information is validated within the firebase. Another test was run to determine if the fields were updated currently within the database should an employee edit their profile.For optimal performance, please run each test seperately. 


