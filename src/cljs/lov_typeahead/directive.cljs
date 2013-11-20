(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(def theModule (.module js/angular "lovTypeahead" (clj->js [])))
(.directive theModule
  "lovTypeahead"
  (fn []
    (clj->js
      {:link (fn [scope element attrs] 
               (let [name (.-lovTypeahead attrs)
                     prefetch (.-lovPrefetch attrs)
                     limit (.-lovLimit attrs)
                     value-key (.-lovValueKey attrs)
                     lov-model (.-lovModel attrs)
                     update-model (fn [event datum name]
                                    (.log js/console (str "update model, dataset name: " name))
                                    (.log js/console (str "update model, datum: " (.stringify js/JSON datum)))
                                    (.log js/console (str "update model, datum.object: " (.stringify js/JSON (.-object datum))))
                                    (.log js/console (str "update model, lov-model: " lov-model))
                                    (.log js/console (str "update model, value before update: " (.stringify js/JSON (aget scope lov-model))))
                                    (aset scope lov-model (.-object datum))
                                    (.log js/console (str "update model, value after update: " (.stringify js/JSON (aget scope lov-model))))
                                    (.$digest scope))]
                 (doto element
                   (.typeahead (clj->js {:name name
                                         :prefetch (clj->js {:url prefetch
                                                             :filter #(d/json->dataset value-key %)})
                                         :limit limit}))
                   (.on "typeahead:selected" update-model)
                   (.on "typeahead:autocompleted" update-model))))})))