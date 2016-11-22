'use strict';

angular.module('todoListApp')
.directive('todo', function(){
  return {
    // restrict : 'A', // 'Attribute name' so that <div todo></div> works
    restrict : 'E', // 'Element name' so that <todo></todo> works
    templateUrl: 'templates/todo.html',
    replace: true,
    controller: 'todoCtrl'
  }
});