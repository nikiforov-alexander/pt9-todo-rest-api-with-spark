// "prevents certain actions from being taken
// "and throws more exceptions"
// see http://stackoverflow.com/questions/1335851/what-does-use-strict-do-in-javascript-and-what-is-the-reasoning-behind-it
// for more
'use strict';

// starts EXISTING angular app
angular.module('todoListApp')
// if we write
// angular.module('todoListApp', []);
// we will start empty todoListApp, and
// and in this project I won't be able to do anything.

.controller('mainCtrl', function($scope, Todo){
  
  $scope.todos = Todo.query();
  
  $scope.addTodo = function() {
    var todo = new Todo();
    todo.name = 'New task!';
    todo.completed = false;
    $scope.todos.unshift(todo);
  };
  
});