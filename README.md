# Techdegree project 9
### REST API with Spark and Angular: "My Todos"
<hr>
### Table of Contents
### Installation instructions
* [Eclipse installation instructions.] (#eclipse)

<hr>

### Misc
- [Quick Links to files and directories] (#links)

<hr>

### Tasks
* [1.] (#task-1)
    This API is versioned, all routes should be prefixed with /api/v1
    <hr>
* [2.] (#task-2)
    When the app first starts it will attempt to fetch all
    Todos in the system.
    Handle the request and return all the Todos.
    Look at the browser tool to see what is being requested
    and how and create the appropriate route
    <hr>
* [3.] (#task-3)
    When a Todo is created and the save link is clicked, 
    it will make a request to the server. 
    Handle the request by creating a Todo and setting the proper status code.
    Look at the browser tool to see what is being requested 
    and how and create the appropriate route
* [4.] (#task-4)
    When a previously saved Todo is deleted and the save link is clicked, 
    it will make a request to the server. 
    Handle the deletion and return 
    a blank response and the proper status code.
    <hr>
* [5.] (#task-5)
    Add unit tests to test your model and dao implementation
    <hr>
* [6.] (#task-6)
    Add functional testing to prove the API is working as expected
    <hr>


<!--Links-->
<!--External Links-->
[spark_blog_readme]: 
    https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse "https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse"
<!--HTML-->
[todo.html]:
    ./src/main/resources/public/templates/todo.html "./src/main/resources/public/templates/todo.html"
[index.html]:
    ./src/main/resources/public/index.html "./src/main/resources/public/index.html"
<!--Java files-->
[ApiResponse]:
    ./src/test/java/com/teamtreehouse/techdegrees/testing/ApiResponse.java "./src/test/java/com/teamtreehouse/techdegrees/testing/ApiResponse.java"
[ApiClient]:
    ./src/test/java/com/teamtreehouse/techdegrees/testing/ApiClient.java "./src/test/java/com/teamtreehouse/techdegrees/testing/ApiClient.java"
[TodoDaoImplTest]:
    ./src/test/java/com/teamtreehouse/techdegrees/dao/TodoDaoImplTest.java "./src/test/java/com/teamtreehouse/techdegrees/dao/TodoDaoImplTest.java"
[AppTest]:
    ./src/test/java/com/teamtreehouse/techdegrees/AppTest.java "./src/test/java/com/teamtreehouse/techdegrees/AppTest.java"
[TodoTask]:
    ./src/main/java/com/teamtreehouse/techdegrees/model/TodoTask.java "./src/main/java/com/teamtreehouse/techdegrees/model/TodoTask.java"
[DaoException]:
    ./src/main/java/com/teamtreehouse/techdegrees/exception/DaoException.java "./src/main/java/com/teamtreehouse/techdegrees/exception/DaoException.java"
[ApiError]:
    ./src/main/java/com/teamtreehouse/techdegrees/exception/ApiError.java "./src/main/java/com/teamtreehouse/techdegrees/exception/ApiError.java"
[App]:
    ./src/main/java/com/teamtreehouse/techdegrees/App.java "./src/main/java/com/teamtreehouse/techdegrees/App.java"
[TodoDaoImpl]:
    ./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDaoImpl.java "./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDaoImpl.java"
[TodoDao]:
    ./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDao.java "./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDao.java"
<!--Properties files-->
[build.gradle]:
    build.gradle "Gradle configuration file: build.gradle"
[initial_project_files]:
    initial-project-files "directory with initial project files from Treeshouse"
[instateam.mv.db]: 
    data/instateam.mv.db "H2 databased used in project: instateam.mv.db"
[h2-1.4.192.jar]:
    h2-1.4.192.jar "H2 database jar file, used to launch server h2-1.4.192.jar"
[.project]:
    .project "Eclipse .project file, generated by IntellijIdea"
[.userlibraries]:
    pt9-todo-rest-api-with-spark.userlibraries "Eclipse .userlibraries file, generated by IntellijIdea: pt9-todo-rest-api-with-spark.userlibraries"
<!--SQL files-->    
[init.sql]:
    ./src/main/resources/db/init.sql "./src/main/resources/db/init.sql"

### Eclipse Installation instructions
<hr> <a id="eclipse"></a>
I generated necessary [.project] and
[.userlibraries] and added `apply plugin : 'eclipse'` line to
[build.gradle]. ... Under construction
<hr>


### Tasks
1. <a id="task-1"></a>
    This API is versioned, all routes should be prefixed with /api/v1
    <hr>
    I added in [App] class `static final String API_CONTEXT` that is
    attached to every route I have:
    - get("/api/v1/todos"): home page print all todos
    - post("/api/v1/todos"): create new todo request
    - put("/api/v1/todos/:id"): update existing request
    - delete("/api/v1/todos/:id"): deleted existing request
<hr>
2. <a id="task-2"></a>
    When the app first starts it will attempt to fetch all
    Todos in the system.
    Handle the request and return all the Todos.
    Look at the browser tool to see what is being requested
    and how and create the appropriate route
    <hr>
    - get("/api/v1/todos") lambda in [App] is used to process request.
    - integration test [AppTest] method is
    `getRequestToIndexPageReturnsListOfTodos`
    - `findAll()` method in [TodoDaoImpl] is used to fetch all todos.
    - It is tested in [TodoDaoImplTest] class in
    test method called `existingTodosCanBeFoundByFindAll`
<hr>
3. <a id="task-3"></a>
    When a Todo is created and the save link is clicked, 
    it will make a request to the server. 
    Handle the request by creating a Todo and setting the proper status code.
    Look at the browser tool to see what is being requested 
    and how and create the appropriate route
    <hr>
    post("/api/v1/todos/:id") lambda in [App] handles request to update
    Todo:
    1. We find by id, if such task exists, if not we throw [ApiError],
      handled in `exception` lambda.
    2. We get from request new TodoTask to update. 
    3. We set id to this new Task
    4. we call `todoDao.update` method
<hr>
4. <a id="task-4"></a>
    When a previously saved Todo is deleted and the save link is clicked, 
    it will make a request to the server. 
    Handle the deletion and return 
    a blank response and the proper status code.
    <hr>
    - delete("/api/v1/todos/:id") lambda in App handles deletion of todo.
    - Integration test is [AppTest] method 
    `deletingTodoTaskActuallyDeletedTodoTask`.
    - status is 204
    - request has empty body
    <br>
    - `delete` method in [TodoDaoImpl] is used to delete todo from db.
    - It is tested in [TodoDaoImplTest] class in method `deletingTodoWorks`. 
    <br>
<hr>
### Extra Credit 
5. <a id="task-5"></a>
    Add unit tests to test your model and dao implementation
    <hr>
    [TodoDaoImplTest] was implemented with following tests:
    - `savingTodoSetsId` - checks successful saving of todo task
    - `existingTodosCanBeFoundById` - not needed in project, but let it be.
    - `existingTodosCanBeFoundByFindAll` - checks `findAll()` method
    - `updateTodosWorks` - checks successful update of todoTask
    - `deletingTodoWorks` - checks successful delete of todoTask
<hr>
6. <a id="task-6"></a>
    Add functional testing to prove the API is working as expected
    [AppTest] class was implemented with following tests:
    - `addingTodoReturnsCreatedStatus` - checks 201 status and response body
    after successful addition of todo.
    - `getRequestToIndexPageReturnsListOfTodos` - checks 200 status and response
    body of homepage listing all todos request. 
    - `updatingTodoTaskActuallyUpdatesTodoTask` - checks 200 status and response
    body of updating Todo. Here response body is not empty, but It is said that
    it is optional whether make body empty or not...So for now leave 
    it as it is.
    - `deletingTodoTaskActuallyDeletedTodoTask` - checks 204 status and
    empty response body.
    - `putRequestToNonExistingTodoReturnsNotFoundStatus` - check 404 status
    and response body with error message.
    - `putRequestWithWrongParameterReturnsServerError` - check 500 status and
    response body with error message (optional test).
<hr>

### Quick Links <a id="links"></a>

#### External Links
[spark_blog_readme]: 
    https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse "https://github.com/nikiforov-alexander/pt4-spark-blog#eclipse"
    
#### HTML
[todo.html]:
    ./src/main/resources/public/templates/todo.html "./src/main/resources/public/templates/todo.html"
[index.html]:
    ./src/main/resources/public/index.html "./src/main/resources/public/index.html"
    
#### Java files
[ApiResponse]:
    ./src/test/java/com/teamtreehouse/techdegrees/testing/ApiResponse.java "./src/test/java/com/teamtreehouse/techdegrees/testing/ApiResponse.java"
[ApiClient]:
    ./src/test/java/com/teamtreehouse/techdegrees/testing/ApiClient.java "./src/test/java/com/teamtreehouse/techdegrees/testing/ApiClient.java"
[ApiErrorModel]:
    ./src/test/java/com/teamtreehouse/techdegrees/testing/ApiErrorModel.java "./src/test/java/com/teamtreehouse/techdegrees/testing/ApiErrorModel.java"
[TodoDaoImplTest]:
    ./src/test/java/com/teamtreehouse/techdegrees/dao/TodoDaoImplTest.java "./src/test/java/com/teamtreehouse/techdegrees/dao/TodoDaoImplTest.java"
[AppTest]:
    ./src/test/java/com/teamtreehouse/techdegrees/AppTest.java "./src/test/java/com/teamtreehouse/techdegrees/AppTest.java"
[TodoTask]:
    ./src/main/java/com/teamtreehouse/techdegrees/model/TodoTask.java "./src/main/java/com/teamtreehouse/techdegrees/model/TodoTask.java"
[DaoException]:
    ./src/main/java/com/teamtreehouse/techdegrees/exception/DaoException.java "./src/main/java/com/teamtreehouse/techdegrees/exception/DaoException.java"
[ApiError]:
    ./src/main/java/com/teamtreehouse/techdegrees/exception/ApiError.java "./src/main/java/com/teamtreehouse/techdegrees/exception/ApiError.java"
[App]:
    ./src/main/java/com/teamtreehouse/techdegrees/App.java "./src/main/java/com/teamtreehouse/techdegrees/App.java"
[TodoDaoImpl]:
    ./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDaoImpl.java "./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDaoImpl.java"
[TodoDao]:
    ./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDao.java "./src/main/java/com/teamtreehouse/techdegrees/dao/TodoDao.java"
    
#### Properties files
[build.gradle]:
    build.gradle "Gradle configuration file: build.gradle"
[initial_project_files]:
    initial-project-files "directory with initial project files from Treeshouse"
[my-todos.mv.db]: 
    data/my-todos.mv.db "H2 databased used in project: my-todos.mv.db"
[h2-1.4.192.jar]:
    h2-1.4.192.jar "H2 database jar file, used to launch server h2-1.4.192.jar"
[.project]:
    .project "Eclipse .project file, generated by IntellijIdea"
[.userlibraries]:
    pt9-todo-rest-api-with-spark.userlibraries "Eclipse .userlibraries file, generated by IntellijIdea: pt9-todo-rest-api-with-spark.userlibraries"
    
#### SQL files
[init.sql]:
    ./src/main/resources/db/init.sql "File creating table if not exist ./src/main/resources/db/init.sql"

