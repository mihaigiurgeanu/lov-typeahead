goog.provide('lov_typeahead.utils');
goog.require('cljs.core');
/**
* @param {...*} var_args
*/
lov_typeahead.utils.log = (function() { 
var log__delegate = function (more){return console.log(cljs.core.apply.call(null,cljs.core.str,more));
};
var log = function (var_args){
var more = null;if (arguments.length > 0) {
  more = cljs.core.array_seq(Array.prototype.slice.call(arguments, 0),0);} 
return log__delegate.call(this,more);};
log.cljs$lang$maxFixedArity = 0;
log.cljs$lang$applyTo = (function (arglist__4674){
var more = cljs.core.seq(arglist__4674);
return log__delegate(more);
});
log.cljs$core$IFn$_invoke$arity$variadic = log__delegate;
return log;
})()
;
/**
* makes a javascript map from a clojure one
*/
lov_typeahead.utils.clj__GT_js = (function clj__GT_js(cljmap){var out = {};cljs.core.doall.call(null,cljs.core.map.call(null,(function (p1__4675_SHARP_){return (out[cljs.core.name.call(null,cljs.core.first.call(null,p1__4675_SHARP_))] = cljs.core.second.call(null,p1__4675_SHARP_));
}),cljmap));
return out;
});
/**
* Recursively converts a sequential object into a JavaScript array
*/
lov_typeahead.utils.jsArr = (function jsArr(seq){return cljs.core.vec.call(null,cljs.core.map.call(null,(function (p1__4676_SHARP_){if(cljs.core.sequential_QMARK_.call(null,p1__4676_SHARP_))
{return jsArr.call(null,p1__4676_SHARP_);
} else
{return p1__4676_SHARP_;
}
}),seq)).array();
});
/**
* Convert a clojure map into a JavaScript object
*/
lov_typeahead.utils.jsObj = (function jsObj(obj){return cljs.core.into.call(null,cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.call(null,(function (p__4679){var vec__4680 = p__4679;var k = cljs.core.nth.call(null,vec__4680,0,null);var v = cljs.core.nth.call(null,vec__4680,1,null);var k__$1 = (((k instanceof cljs.core.Keyword))?cljs.core.name.call(null,k):k);var v__$1 = (((v instanceof cljs.core.Keyword))?cljs.core.name.call(null,v):v);if(cljs.core.map_QMARK_.call(null,v__$1))
{return cljs.core.PersistentVector.fromArray([k__$1,jsObj.call(null,v__$1)], true);
} else
{return cljs.core.PersistentVector.fromArray([k__$1,v__$1], true);
}
}),obj)).strobj();
});
