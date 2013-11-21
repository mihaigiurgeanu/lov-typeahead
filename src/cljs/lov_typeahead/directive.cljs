(ns lov-typeahead.directive
  (:require [lov-typeahead.dataset :as d]))

(defn link-typeahead 
  "The directive linking function that configs the typeahead element"
  [scope element attrs] (let [limit (.-lovLimit attrs)
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
                                             (.$digest scope))
                              set-up-typeahead (fn []
                                                 (.log js/console (str "new value of lov-typeahead: " (.-lovTypeahead attrs)))
                                                 (.typeahead element "destroy")
                                                 (.log js/console "setup the typehead")
                                                 (.typeahead element 
                                                   (let [name (.-lovTypeahead attrs)
                                                         prefetch (.-lovPrefetch attrs)
                                                         remote (.-lovRemote attrs)
                                                         options {:name (str name "-123")}
                                                         filter-fn #(d/json->dataset value-key %)
                                                         options (if (nil? prefetch) options 
                                                                   (assoc options :prefetch (clj->js {:url prefetch
                                                                                                      :filter filter-fn})))
                                                         options (if (nil? remote) options
                                                                   (assoc options :remote (clj->js {:url remote
                                                                                                    :filter filter-fn})))]
                                                     (.log js/console (str "prefetch: " prefetch))
                                                     (clj->js options))))]
                          (.$observe attrs "lovTypeahead" set-up-typeahead)
                          (.$observe attrs "lovRemote" set-up-typeahead)
                          (.$watch scope lov-model (fn [value] (.val element (aget value value-key))))
                          (doto element
                            (.on "typeahead:selected" update-model)
                            (.on "typeahead:autocompleted" update-model))))

(def lovTypeaheadModule
   "The definition of the lov-typeahead's module" 
  (.module js/angular "lovTypeahead" (clj->js [])))

(.directive lovTypeaheadModule
  "lovTypeahead"
  (fn []
    (clj->js {:link link-typeahead})))

