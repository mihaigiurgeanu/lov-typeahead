goog.provide('lov_typeahead.directive');
goog.require('cljs.core');
lov_typeahead.directive.theModule = angular.module("lovTypeaheadDirecive",cljs.core.clj__GT_js.call(null,cljs.core.PersistentVector.EMPTY));
lov_typeahead.directive.theModule.directive((function (){return cljs.core.clj__GT_js.call(null,cljs.core.PersistentArrayMap.fromArray([new cljs.core.Keyword(null,"link","link",1017226092),(function (scope,element,attrs){var label = "label";return scope.$watch(attrs.ngModel,(function (value){return cljs.core.get.call(null,value,label);
}));
})], true));
}));
