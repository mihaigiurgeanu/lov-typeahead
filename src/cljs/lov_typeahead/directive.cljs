(ns lov-typeahead.directive)

(def theModule (.module js/angular "lovTypeahead" (clj->js [])))
(.directive theModule
  "lovTypeahead"
  (fn []
    (clj->js
      {:link (fn [scope element attrs] 
               (let [name (.-lovName attrs)
                     prefetch (.-lovPrefetch attrs)
                     limit (.-lovLimit attrs)]
                 (.typeahead element (clj->js {:name name
                                               :prefetch prefetch
                                               :limit limit}))))})))