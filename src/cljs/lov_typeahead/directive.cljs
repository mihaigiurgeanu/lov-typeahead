(ns lov-typeahead.directive)

(def theModule (.module js/angular "lovTypeaheadDirecive" (clj->js [])))
(.directive theModule
  "lovTypeahead"
  (fn []
    (clj->js
      {:link (fn [scope element attrs] 
               (let [label "label"]
                 (.$watch scope (.-ngModel attrs)
                   (fn [value] (get value label)))))})))