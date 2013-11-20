(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(def theModule (.module js/angular "lovTypeahead" (clj->js [])))
(.directive theModule
  "lovTypeahead"
  (fn []
    (clj->js
      {:link (fn [scope element attrs] 
               (let [name (.-lovName attrs)
                     prefetch (.-lovPrefetch attrs)
                     limit (.-lovLimit attrs)
                     value-key (.-lovValueKey attrs)]
                 (.typeahead element (clj->js {:name name
                                               :prefetch (clj->js {:url prefetch
                                                                   :filter #(d/json->dataset value-key %)})
                                               :limit limit}))))})))